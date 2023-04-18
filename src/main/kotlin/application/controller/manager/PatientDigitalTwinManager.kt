/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.patient.Patient
import entity.patient.PatientData

/** This interface model the operation on Patients Digital Twins. */
interface PatientDigitalTwinManager {

    /**
     * Create the digital twin of a [Patient] given its [patientId].
     */
    fun createPatientDT(
        patientId: PatientData.PatientId,
    ): Patient?
}
