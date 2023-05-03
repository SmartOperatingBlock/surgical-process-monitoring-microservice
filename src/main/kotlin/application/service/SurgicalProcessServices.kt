/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import usecase.repository.SurgicalProcessRepository
import java.time.Instant

/**
 * Module that contains all [ApplicationService] of surgical processes.
 */
object SurgicalProcessServices {

    /**
     * The Application Service to create a [SurgicalProcess].
     */
    class CreateSurgicalProcess(
        private val surgicalProcess: SurgicalProcess,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<SurgicalProcess?> {
        override fun execute(): SurgicalProcess? = surgicalProcessRepository.createSurgicalProcess(surgicalProcess)
    }

    /**
     * The Application Service to get a [SurgicalProcess] by a [surgicalProcessId].
     */
    class GetSurgicalProcessById(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<SurgicalProcess?> {
        override fun execute(): SurgicalProcess? = surgicalProcessRepository.getSurgicalProcessById(surgicalProcessId)
    }

    /**
     * The Application Service to get the current [SurgicalProcess] within the Operating Block.
     */
    class GetCurrentSurgicalProcesses(
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<Set<SurgicalProcess>> {
        override fun execute(): Set<SurgicalProcess> = surgicalProcessRepository.getCurrentSurgicalProcesses()
    }

    /**
     * The Application Service to update the [state] of a [SurgicalProcess] by its [surgicalProcessId].
     */
    class UpdateSurgicalProcessState(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val dateTime: Instant,
        private val state: ProcessData.ProcessState,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            surgicalProcessRepository.updateSurgicalProcessState(surgicalProcessId, dateTime, state)
    }

    /**
     * The Application Service to update the [step] of a [SurgicalProcess] by its [surgicalProcessId].
     */
    class UpdateSurgicalProcessStep(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val dateTime: Instant,
        private val step: ProcessData.ProcessStep,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            surgicalProcessRepository.updateSurgicalProcessStep(surgicalProcessId, dateTime, step)
    }

    /**
     * The Application Service to update the [room] of a [SurgicalProcess] by its [surgicalProcessId].
     */
    class UpdateSurgicalProcessRoom(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val latestRoomId: String?,
        private val room: Room,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            surgicalProcessRepository.updateSurgicalProcessRoom(surgicalProcessId, latestRoomId, room)
    }

    /**
     * The Application Service to get the [ProcessData.ProcessState] of a [SurgicalProcess] by its [surgicalProcessId].
     */
    class GetSurgicalProcessStates(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<List<Pair<Instant, ProcessData.ProcessState>>> {
        override fun execute(): List<Pair<Instant, ProcessData.ProcessState>> =
            surgicalProcessRepository.getSurgicalProcessStates(surgicalProcessId)
    }

    /**
     * The Application Service to get the [ProcessData.ProcessStep] of a [SurgicalProcess] by its [surgicalProcessId].
     */
    class GetSurgicalProcessSteps(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<List<Pair<Instant, ProcessData.ProcessStep>>> {
        override fun execute(): List<Pair<Instant, ProcessData.ProcessStep>> =
            surgicalProcessRepository.getSurgicalProcessSteps(surgicalProcessId)
    }

    /**
     * The Application Service to delete a [SurgicalProcess] by its [surgicalProcessId]
     * using the [surgicalProcessRepository].
     */
    class DeleteSurgicalProcess(
        private val surgicalProcessId: ProcessData.ProcessId,
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            surgicalProcessRepository.deleteSurgicalProcess(surgicalProcessId)
    }
}
