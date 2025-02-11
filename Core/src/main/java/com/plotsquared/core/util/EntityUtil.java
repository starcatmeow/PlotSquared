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
package com.plotsquared.core.util;

import com.plotsquared.core.configuration.Settings;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.PlotFlag;
import com.plotsquared.core.plot.flag.implementations.DoneFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.plotsquared.core.util.entity.EntityCategories.CAP_ANIMAL;
import static com.plotsquared.core.util.entity.EntityCategories.CAP_ENTITY;
import static com.plotsquared.core.util.entity.EntityCategories.CAP_MISC;
import static com.plotsquared.core.util.entity.EntityCategories.CAP_MOB;
import static com.plotsquared.core.util.entity.EntityCategories.CAP_MONSTER;
import static com.plotsquared.core.util.entity.EntityCategories.CAP_VEHICLE;

/**
 * Entity related general utility methods
 */
public class EntityUtil {

    private EntityUtil() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    private static int capNumeral(final @NonNull String flagName) {
        int i;
        switch (flagName) {
            case "mob-cap":
                i = CAP_MOB;
                break;
            case "hostile-cap":
                i = CAP_MONSTER;
                break;
            case "animal-cap":
                i = CAP_ANIMAL;
                break;
            case "vehicle-cap":
                i = CAP_VEHICLE;
                break;
            case "misc-cap":
                i = CAP_MISC;
                break;
            case "entity-cap":
            default:
                i = CAP_ENTITY;
        }
        return i;
    }

    public static boolean checkEntity(Plot plot, PlotFlag<Integer, ?>... flags) {
        if (Settings.Done.RESTRICT_BUILDING && DoneFlag.isDone(plot)) {
            return true;
        }
        int[] mobs = null;
        for (PlotFlag<Integer, ?> flag : flags) {
            final int i = capNumeral(flag.getName());
            int cap = plot.getFlag(flag);
            if (cap == Integer.MAX_VALUE) {
                continue;
            }
            if (cap == 0) {
                return true;
            }
            if (mobs == null) {
                mobs = plot.countEntities();
            }
            if (mobs[i] >= cap) {
                plot.setMeta("EntityCount", mobs);
                plot.setMeta("EntityCountTime", System.currentTimeMillis());
                plot.debug("Prevented spawning of mob because it would exceed " + flag.getName());
                return true;
            }
        }
        if (mobs != null) {
            for (PlotFlag<Integer, ?> flag : flags) {
                final int i = capNumeral(flag.getName());
                mobs[i]++;
            }
            plot.setMeta("EntityCount", mobs);
            plot.setMeta("EntityCountTime", System.currentTimeMillis());
        }
        return false;
    }

}
