/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller

import application.controller.manager.PatientMedicalDataDatabaseManager
import entity.patient.PatientData
import usecase.repository.PatientRepository
import java.time.Instant

/**
 * The implementation of the [PatientRepository].
 */
class PatientDataController(
    private val patientMedicalDataDatabaseManager: PatientMedicalDataDatabaseManager
) : PatientRepository {

    override fun updatePatientMedicalData(
        patientId: PatientData.PatientId,
        medicalData: PatientData.MedicalData,
        dateTime: Instant
    ): Boolean = patientMedicalDataDatabaseManager.updatePatientMedicalData(patientId, medicalData, dateTime)

    override fun getPatientMedicalData(
        patientId: PatientData.PatientId,
        from: Instant,
        to: Instant
    ): Map<Instant, PatientData.MedicalData> =
        patientMedicalDataDatabaseManager.getPatientMedicalData(patientId, from, to)

    override fun getCurrentPatientMedicalData(patientId: PatientData.PatientId): PatientData.MedicalData? =
        patientMedicalDataDatabaseManager.getCurrentPatientMedicalData(patientId)
}
