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
import entity.patient.Patient

/**
 * The module for the Patient Azure Digital Twin.
 */
object PatientAdt {

    private const val PATIENT_MODEL = "dtmi:io:github:smartoperatingblock:Patient;1"

    /**
     * Convert a [Patient] to a Digital Twin.
     * Specifically this extension method will convert it into the Azure Digital Twins SDK [BasicDigitalTwin].
     * @return the corresponding [BasicDigitalTwin].
     */
    fun Patient.toDigitalTwin(): BasicDigitalTwin =
        BasicDigitalTwin(this.id.id)
            .setMetadata(BasicDigitalTwinMetadata().setModelId(PATIENT_MODEL))
}
