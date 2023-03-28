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

/** This interface models the operation on Digital Twins of [SurgicalProcess]. */
interface ProcessDigitalTwinManager {

    /**
     * Create the digital twin of the surgical [process].
     */
    fun createSurgicalProcess(process: SurgicalProcess): Boolean

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
