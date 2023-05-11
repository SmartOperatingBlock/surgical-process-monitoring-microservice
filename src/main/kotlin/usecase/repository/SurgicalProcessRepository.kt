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
import entity.room.Room
import java.time.Instant

/**
 * Interface that models the repository to manage the processes.
 */
interface SurgicalProcessRepository {
    /**
     * Create a [SurgicalProcess].
     * @return null if the process already exist, the process otherwise.
     */
    fun createSurgicalProcess(
        process: SurgicalProcess,
    ): SurgicalProcess?

    /**
     * Get a [SurgicalProcess] by its id.
     * @return the process if it exists, null otherwise.
     */
    fun getSurgicalProcessById(
        processId: ProcessData.ProcessId,
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
        dateTime: Instant,
        state: ProcessData.ProcessState,
    ): Boolean

    /**
     * Update the [step] of a [SurgicalProcess] by its [processId].
     */
    fun updateSurgicalProcessStep(
        processId: ProcessData.ProcessId,
        dateTime: Instant,
        step: ProcessData.ProcessStep,
    ): Boolean

    /**
     * Update the [room] and the previous [latestRoomId] of a [SurgicalProcess] by its [processId].
     */
    fun updateSurgicalProcessRoom(
        processId: ProcessData.ProcessId,
        latestRoomId: String?,
        room: Room,
    ): Boolean

    /**
     * Get a list of pair of [Instant] and [ProcessData.ProcessState] given the [ProcessData.ProcessId].
     */
    fun getSurgicalProcessStates(
        surgicalProcessId: ProcessData.ProcessId,
    ): List<Pair<Instant, ProcessData.ProcessState>>

    /**
     * Get a list of pair of [Instant] and [ProcessData.ProcessStep] given the [ProcessData.ProcessId].
     */
    fun getSurgicalProcessSteps(
        surgicalProcessId: ProcessData.ProcessId,
    ): List<Pair<Instant, ProcessData.ProcessStep>>

    /**
     * Delete a [SurgicalProcess] by it [surgicalProcessId].
     */
    fun deleteSurgicalProcess(
        surgicalProcessId: ProcessData.ProcessId,
    ): Boolean
}
