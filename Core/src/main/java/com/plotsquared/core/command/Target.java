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

import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.StringMan;
import com.plotsquared.core.util.query.PlotQuery;
import net.kyori.adventure.text.minimessage.Template;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandDeclaration(command = "target",
        usage = "/plot target <<X;Z> | nearest>",
        permission = "plots.target",
        requiredType = RequiredType.PLAYER,
        category = CommandCategory.INFO)
public class Target extends SubCommand {

    public Target() {
        super();
    }

    @Override
    public boolean onCommand(PlotPlayer<?> player, String[] args) {
        Location location = player.getLocation();
        if (!location.isPlotArea()) {
            player.sendMessage(TranslatableCaption.of("errors.not_in_plot_world"));
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(
                    TranslatableCaption.of("commandconfig.command_syntax"),
                    Template.of("value", "/plot target <<X;Z> | nearest>")
            );
            return false;
        }
        Plot target = null;
        if (StringMan.isEqualIgnoreCaseToAny(args[0], "near", "nearest")) {
            int distance = Integer.MAX_VALUE;
            for (Plot plot : PlotQuery.newQuery().inWorld(location.getWorldName())) {
                double current = plot.getCenterSynchronous().getEuclideanDistanceSquared(location);
                if (current < distance) {
                    distance = (int) current;
                    target = plot;
                }
            }
            if (target == null) {
                player.sendMessage(TranslatableCaption.of("invalid.found_no_plots"));
                return false;
            }
        } else if ((target = Plot.getPlotFromString(player, args[0], true)) == null) {
            return false;
        }
        target.getCenter(player::setCompassTarget);
        player.sendMessage(
                TranslatableCaption.of("compass.compass_target"),
                Template.of("target", target.toString())
        );
        return true;
    }

    @Override
    public Collection<Command> tab(final PlotPlayer<?> player, String[] args, boolean space) {
        return Stream.of("<X;Z>", "nearest")
                .filter(value -> value.startsWith(args[0].toLowerCase(Locale.ENGLISH)))
                .map(value -> new Command(null, false, value, "plots.target", RequiredType.PLAYER, null) {
                }).collect(Collectors.toList());
    }

}
