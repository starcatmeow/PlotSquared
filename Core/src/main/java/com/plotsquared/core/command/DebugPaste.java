/*
 *       _____  _       _    _____                                _
 *      |  __ \| |     | |  / ____|                              | |
 *      | |__) | | ___ | |_| (___   __ _ _   _  __ _ _ __ ___  __| |
 *      |  ___/| |/ _ \| __|\___ \ / _` | | | |/ _` | '__/ _ \/ _` |
 *      | |    | | (_) | |_ ____) | (_| | |_| | (_| | | |  __/ (_| |
 *      |_|    |_|\___/ \__|_____/ \__, |\__,_|\__,_|_|  \___|\__,_|
 *                                    | |
 *                                    |_|
 *            PlotSquared plot management system for Minecraft
 *               Copyright (C) 2014 - 2022 IntellectualSites
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.core.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.intellectualsites.paster.IncendoPaster;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.configuration.Settings;
import com.plotsquared.core.configuration.Storage;
import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.inject.annotations.ConfigFile;
import com.plotsquared.core.inject.annotations.WorldFile;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.util.PremiumVerification;
import com.plotsquared.core.util.task.TaskManager;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@CommandDeclaration(command = "debugpaste",
        aliases = "dp",
        usage = "/plot debugpaste",
        permission = "plots.debugpaste",
        category = CommandCategory.DEBUG,
        confirmation = true,
        requiredType = RequiredType.NONE)
public class DebugPaste extends SubCommand {

    private final File configFile;
    private final File worldfile;

    @Inject
    public DebugPaste(
            @ConfigFile final @NonNull File configFile,
            @WorldFile final @NonNull File worldFile
    ) {
        this.configFile = configFile;
        this.worldfile = worldFile;
    }

    @Override
    public boolean onCommand(final PlotPlayer<?> player, String[] args) {
        TaskManager.runTaskAsync(() -> {
            try {
                StringBuilder b = new StringBuilder();
                b.append(
                        """
                         # Welcome to this paste
                         # It is meant to provide us at IntellectualSites with better information about your problem
                         """
                );
                b.append("# PlotSquared Information\n");
                b.append("PlotSquared Version: ").append(PlotSquared.get().getVersion())
                        .append("\n");
                b.append("Database Type: ").append(Storage.MySQL.USE ? "MySQL" : "SQLite").append("\n");
                b.append("Resource ID: ").append(PremiumVerification.getResourceID()).append("\n");
                b.append("Download ID: ").append(PremiumVerification.getDownloadID()).append("\n");
                b.append("This PlotSquared version is licensed to the spigot user ")
                        .append(PremiumVerification.getUserID()).append("\n\n");
                b.append("# WorldEdit implementation:\n");
                b.append(PlotSquared.platform().worldEditImplementations()).append("\n\n");
                b.append("# Server Information\n");
                b.append("Server Version: ").append(PlotSquared.platform().serverImplementation())
                        .append("\n");
                b.append("online_mode: ").append(!Settings.UUID.OFFLINE).append(';')
                        .append(!Settings.UUID.OFFLINE).append('\n');
                b.append(PlotSquared.platform().pluginsFormatted());
                b.append("\n\n# YAY! Now, let's see what we can find in your JVM\n");
                Runtime runtime = Runtime.getRuntime();
                RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                b.append("Uptime: ")
                        .append(TimeUnit.MINUTES.convert(rb.getUptime(), TimeUnit.MILLISECONDS))
                        .append(" minutes")
                        .append('\n');
                b.append("JVM Flags: ").append(rb.getInputArguments()).append('\n');
                b.append("Free Memory: ").append(runtime.freeMemory() / 1024 / 1024).append(" MB")
                        .append('\n');
                b.append("Max Memory: ").append(runtime.maxMemory() / 1024 / 1024).append(" MB")
                        .append('\n');
                b.append("Total Memory: ").append(runtime.totalMemory() / 1024 / 1024).append(" MB")
                        .append('\n');
                b.append("Available Processors: ").append(runtime.availableProcessors()).append('\n');
                b.append("Java Name: ").append(rb.getVmName()).append('\n');
                b.append("Java Version: '").append(System.getProperty("java.version"))
                        .append("'\n");
                b.append("Java Vendor: '").append(System.getProperty("java.vendor")).append("'\n");
                b.append("Operating System: '").append(System.getProperty("os.name")).append("'\n");
                b.append("OS Version: ").append(System.getProperty("os.version")).append('\n');
                b.append("OS Arch: ").append(System.getProperty("os.arch")).append('\n');
                b.append("# Okay :D Great. You are now ready to create your bug report!");
                b.append(
                        "\n# You can do so at https://github.com/IntellectualSites/PlotSquared/issues");
                b.append("\n# or via our Discord at https://discord.gg/intellectualsites");

                final IncendoPaster incendoPaster = new IncendoPaster("plotsquared");
                incendoPaster.addFile(new IncendoPaster.PasteFile("information", b.toString()));

                try {
                    final File logFile =
                            new File("logs/latest.log");
                    if (Files.size(logFile.toPath()) > 14_000_000) {
                        throw new IOException(
                                "The latest.log is larger than 14MB. Please reboot your server and submit a new paste.");
                    }
                    incendoPaster
                            .addFile(logFile);
                } catch (IOException ignored) {
                    player.sendMessage(
                            TranslatableCaption.of("debugpaste.latest_log"),
                            Template.of("file", "latest.log"),
                            Template.of("size", "14MB")
                    );
                }

                try {
                    incendoPaster.addFile(this.configFile);
                } catch (final IllegalArgumentException ignored) {
                    player.sendMessage(
                            TranslatableCaption.of("debugpaste.empty_file"),
                            Template.of("file", "settings.yml")
                    );
                }
                try {
                    incendoPaster.addFile(this.worldfile);
                } catch (final IllegalArgumentException ignored) {
                    player.sendMessage(
                            TranslatableCaption.of("debugpaste.empty_file"),
                            Template.of("file", "worlds.yml")
                    );
                }

                try {
                    final File MultiverseWorlds = new File(
                            PlotSquared.platform().getDirectory(),
                            "../Multiverse-Core/worlds.yml"
                    );
                    incendoPaster.addFile(MultiverseWorlds, "Multiverse-Core/worlds.yml");
                } catch (final IOException ignored) {
                    player.sendMessage(
                            TranslatableCaption.of("debugpaste.skip_multiverse"),
                            Template.of("file", "worlds.yml")
                    );
                }

                try {
                    final String rawResponse = incendoPaster.upload();
                    final JsonObject jsonObject =
                            new JsonParser().parse(rawResponse).getAsJsonObject();

                    if (jsonObject.has("created")) {
                        final String pasteId = jsonObject.get("paste_id").getAsString();
                        final String link =
                                String.format("https://athion.net/ISPaster/paste/view/%s", pasteId);
                        player.sendMessage(
                                TranslatableCaption.of("debugpaste.debug_report_created"),
                                Template.of("url", link)
                        );
                    } else {
                        final String responseMessage = jsonObject.get("response").getAsString();
                        player.sendMessage(
                                TranslatableCaption.of("debugpaste.creation_failed"),
                                Template.of("value", responseMessage)
                        );
                    }
                } catch (final Throwable throwable) {
                    throwable.printStackTrace();
                    player.sendMessage(
                            TranslatableCaption.of("debugpaste.creation_failed"),
                            Template.of("value", throwable.getMessage())
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

}
