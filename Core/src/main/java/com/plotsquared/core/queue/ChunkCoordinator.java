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
package com.plotsquared.core.queue;

public abstract class ChunkCoordinator implements Runnable {

    /**
     * Starts the chunk coordinator. This will usually (implementation-specific-permitting) mark chunks to be loaded in batches,
     * then add them to a queue and apply tickets once loaded to prevent unloading. A repeating task will then iterate over loaded
     * chunks, access them with a Consumer(BlockVector2) and remove the ticket once work has been completed on it.
     */
    public abstract void start();

    /**
     * Cancel the chunk coordinator.
     */
    public abstract void cancel();

    /**
     * Get the amount of remaining chunks (at the time of the method call)
     *
     * @return Snapshot view of remaining chunk count
     */
    public abstract int getRemainingChunks();

    /**
     * Get the amount of requested chunks
     *
     * @return Requested chunk count
     */
    public abstract int getTotalChunks();

}
