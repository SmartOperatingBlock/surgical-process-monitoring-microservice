/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.process.ProcessData
import entity.process.SurgicalProcess

/**
 * This interface models the manager of the database for processes.
 */
interface ProcessDatabaseManager {

    /**
     * Create a [SurgicalProcess].
     * @return null if the process already exist, the process otherwise.
     */
    fun createSurgicalProcess(
        process: SurgicalProcess
    ): SurgicalProcess?

    /**
     * Get a [SurgicalProcess] by its id.
     * @return the process if it exists, null otherwise.
     */
    fun getSurgicalProcessById(
        processId: ProcessData.ProcessId
    ): SurgicalProcess?

    /**
     * Get all current [SurgicalProcess].
     * @return a set of all processes.
     */
    fun getCurrentSurgicalProcesses(): Set<SurgicalProcess>
}