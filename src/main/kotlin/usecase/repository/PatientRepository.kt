/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.patient.PatientData
import java.time.Instant

/**
 * Interface that models the repository to manage the patients medical data.
 */
interface PatientRepository {

    /**
     * Update the [PatientData.MedicalData] of a patient.
     * @return true if correctly updated, false otherwise.
     */
    fun updatePatientMedicalData(
        patientId: PatientData.PatientId,
        medicalData: PatientData.MedicalData,
        dateTime: Instant
    ): Boolean

    /**
     * Get the [PatientData.MedicalData] of a patient given the id and a range of time.
     * @return a map with the instant as key and the medical data as value.
     */
    fun getPatientMedicalData(
        patientId: PatientData.PatientId,
        from: Instant,
        to: Instant
    ): Map<Instant, PatientData.MedicalData>

    /**
     * Get current patient medical data given the patient id.
     * @return the current patient medical data.
     */
    fun getCurrentPatientMedicalData(
        patientId: PatientData.PatientId
    ): PatientData.MedicalData
}
