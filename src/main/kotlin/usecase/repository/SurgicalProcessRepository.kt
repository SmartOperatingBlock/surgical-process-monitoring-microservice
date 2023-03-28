/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.process.ProcessData
import entity.process.SurgicalProcess

/**
 * Interface that models the repository to manage the processes.
 */
interface SurgicalProcessRepository {
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

    /**
     * Update the [state] of a [SurgicalProcess] by its [processId].
     */
    fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean

    /**
     * Update the [step] of a [SurgicalProcess] by its [processId].
     */
    fun updateSurgicalProcessStep(
        processId: ProcessData.ProcessId,
        step: ProcessData.ProcessStep
    ): Boolean
}
