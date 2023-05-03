/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.presenter

import com.azure.digitaltwins.core.BasicDigitalTwin
import com.azure.digitaltwins.core.BasicDigitalTwinMetadata
import entity.process.SurgicalProcess

/**
 * The module for the Surgical Process Azure Digital Twin.
 */
object SurgicalProcessAdt {

    private const val STATE_PROPERTY = "process_state"
    private const val STEP_PROPERTY = "process_step"

    /** The model of the surgical process DT. **/
    const val SURGICAL_PROCESS_MODEL = "dtmi:io:github:smartoperatingblock:SurgicalProcess;1"
    /** The relationship between surgical process and booking dt. */
    const val BOOKING_RELATIONSHIP = "rel_associated_to_booking"
    /** The relationship between surgical process and patient dt. */
    const val PATIENT_RELATIONSHIP = "rel_involve_patient"
    /** The relationship between surgical process and room dt. */
    const val ROOM_RELATIONSHIP = "rel_is_inside"

    /**
     * Convert a [SurgicalProcess] to a Digital Twin.
     * Specifically this extension method will convert it into the Azure Digital Twins SDK [BasicDigitalTwin].
     * @return the corresponding [BasicDigitalTwin].
     */
    fun SurgicalProcess.toDigitalTwin(): BasicDigitalTwin =
        BasicDigitalTwin(this.id.id)
            .setMetadata(BasicDigitalTwinMetadata().setModelId(SURGICAL_PROCESS_MODEL))
            .addToContents(STATE_PROPERTY, this.state.ordinal.toString())
            .addToContents(STEP_PROPERTY, this.step?.ordinal.toString())
}
