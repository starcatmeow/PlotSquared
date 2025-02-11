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
package com.plotsquared.core.events;

import com.plotsquared.core.command.Claim;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * PlayerAutoPlotEvent returns null for {@link PlotEvent#getPlot()} as the event is fired before the plot is chosen.
 */
public class PlayerAutoPlotEvent extends PlotEvent implements CancellablePlotEvent {

    private final PlotPlayer<?> player;
    private final PlotArea plotArea;
    private Result eventResult;
    private String schematic;
    private int sizeX;
    private int sizeZ;

    /**
     * PlayerAutoPlotEvent: called when a player attempts to auto claim a plot.
     *
     * @param player    The player attempting to auto claim
     * @param plotArea  The applicable plot area
     * @param schematic The schematic defined or null
     * @param sizeX     The size of the auto area
     * @param sizeZ     The size of the auto area
     */
    public PlayerAutoPlotEvent(
            PlotPlayer<?> player, PlotArea plotArea, @Nullable String schematic,
            int sizeX, int sizeZ
    ) {
        super(null);
        this.player = player;
        this.plotArea = plotArea;
        this.schematic = schematic;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    /**
     * Returns null as the plots to be claimed haven't been chosen yet. This will depend on the size of the auto
     * ({@link PlayerAutoPlotEvent#setSizeX(int)} and {@link PlayerAutoPlotEvent#setSizeZ(int)}). To see which plots have been
     * chosen, see {@link PlayerAutoPlotsChosenEvent}.
     *
     * @return null
     */
    @Override
    public @Nullable Plot getPlot() {
        return null;
    }

    /**
     * Obtain the schematic string as used by the {@link Claim} command or null.
     *
     * @return schematic string
     */
    public @Nullable String getSchematic() {
        return this.schematic;
    }

    /**
     * Set the schematic string used in the claim.
     *
     * @param schematic the schematic name
     */
    public void setSchematic(String schematic) {
        this.schematic = schematic;
    }

    @Override
    public Result getEventResult() {
        return eventResult;
    }

    @Override
    public void setEventResult(Result e) {
        this.eventResult = e;
    }

    public PlotPlayer<?> getPlayer() {
        return this.player;
    }

    public PlotArea getPlotArea() {
        return this.plotArea;
    }

    /**
     * @deprecated for removal. Use {@link PlayerAutoPlotEvent#getSizeX()}
     */
    @Deprecated(forRemoval = true)
    public int getSize_x() {
        return getSizeX();
    }

    /**
     * @deprecated for removal. Use {@link PlayerAutoPlotEvent#setSizeX(int)} )}
     */
    @Deprecated(forRemoval = true)
    public void setSize_x(int sizeX) {
        setSizeX(sizeX);
    }

    /**
     * @deprecated for removal. Use {@link PlayerAutoPlotEvent#getSizeZ()}
     */
    @Deprecated(forRemoval = true)
    public int getSize_z() {
        return getSizeZ();
    }

    /**
     * @deprecated for removal. Use {@link PlayerAutoPlotEvent#setSizeZ(int)} )}
     */
    @Deprecated(forRemoval = true)
    public void setSize_z(int sizeZ) {
        setSizeZ(sizeZ);
    }

    /**
     * Get the x size of the auto-area
     *
     * @return x size
     */
    public int getSizeX() {
        return this.sizeX;
    }

    /**
     * Set the x size of the auto-area
     *
     * @param sizeX x size
     */
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    /**
     * Get the z size of the auto-area
     *
     * @return z size
     */
    public int getSizeZ() {
        return this.sizeZ;
    }

    /**
     * Set the z size of the auto-area
     *
     * @param sizeZ z size
     */
    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

}
