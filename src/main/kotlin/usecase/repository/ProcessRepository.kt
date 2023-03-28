/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.process.ProcessData

/**
 * Interface that models the repository to manage the processes.
 */
interface ProcessRepository {
    /**
     * Create a [Process].
     * @return null if the process already exist, the process otherwise.
     */
    fun createProcess(
        process: Process
    ): Process?

    /**
     * Get a [Process] by its id.
     * @return the process if it exists, null otherwise.
     */
    fun getProcessById(
        processId: ProcessData.ProcessId
    ): Process?

    /**
     * Get all current [Process].
     * @return a set of all processes.
     */
    fun getCurrentProcesses(): Set<Process>
}
