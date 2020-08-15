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
 *                  Copyright (C) 2020 IntellectualSites
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
package com.plotsquared.core.plot.flag.implementations;

import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.InternalFlag;
import com.plotsquared.core.plot.flag.PlotFlag;

import javax.annotation.Nonnull;

public class DoneFlag extends PlotFlag<String, DoneFlag> implements InternalFlag {

    /**
     * Construct a new flag instance.
     *
     * @param value Flag value
     */
    public DoneFlag(@Nonnull String value) {
        super(value, TranslatableCaption.of("info.none"), TranslatableCaption.of("info.none"));
    }

    public static boolean isDone(final Plot plot) {
        return !plot.getFlag(DoneFlag.class).isEmpty();
    }

    @Override public DoneFlag parse(@Nonnull String input) {
        return flagOf(input);
    }

    @Override public DoneFlag merge(@Nonnull String newValue) {
        return flagOf(newValue);
    }

    @Override public String toString() {
        return this.getValue();
    }

    @Override public String getExample() {
        return "";
    }

    @Override protected DoneFlag flagOf(@Nonnull String value) {
        return new DoneFlag(value);
    }

}
