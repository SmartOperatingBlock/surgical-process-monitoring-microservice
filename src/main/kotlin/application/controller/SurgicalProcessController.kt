/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller

import application.controller.manager.ProcessDatabaseManager
import application.controller.manager.ProcessDigitalTwinManager
import entity.process.ProcessData
import entity.process.SurgicalProcess
import usecase.repository.SurgicalProcessRepository

/**
 * The implementation of the [SurgicalProcessRepository].
 */
class SurgicalProcessController(
    private val processDatabaseManager: ProcessDatabaseManager,
    private val processDigitalTwinManager: ProcessDigitalTwinManager
) : SurgicalProcessRepository {

    override fun createSurgicalProcess(process: SurgicalProcess): SurgicalProcess? =
        if (this.getSurgicalProcessById(process.id) != null) {
            this.processDigitalTwinManager.createSurgicalProcess(process)
            this.processDatabaseManager.createSurgicalProcess(process)
        } else null

    override fun getSurgicalProcessById(processId: ProcessData.ProcessId): SurgicalProcess? =
        this.processDatabaseManager.getSurgicalProcessById(processId)

    override fun getCurrentSurgicalProcesses(): Set<SurgicalProcess> =
        this.processDatabaseManager.getCurrentSurgicalProcesses()

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean =
        this.processDigitalTwinManager.updateSurgicalProcessState(processId, state) &&
            this.processDatabaseManager.updateSurgicalProcessState(processId, state)

    override fun updateSurgicalProcessStep(
        processId: ProcessData.ProcessId,
        step: ProcessData.ProcessStep
    ): Boolean =
        this.processDigitalTwinManager.updateSurgicalProcessStep(processId, step) &&
            this.processDatabaseManager.updateSurgicalProcessStep(processId, step)
}
