/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller

import application.controller.manager.PatientDigitalTwinManager
import application.controller.manager.PatientMedicalDataDatabaseManager
import entity.patient.Patient
import entity.patient.PatientData
import usecase.repository.PatientRepository
import java.time.Instant

/**
 * The implementation of the [PatientRepository].
 */
class PatientDataController(
    private val patientMedicalDataDatabaseManager: PatientMedicalDataDatabaseManager,
    private val patientDigitalTwinManager: PatientDigitalTwinManager,
) : PatientRepository {

    override fun updatePatientMedicalData(
        patientId: PatientData.PatientId,
        medicalData: PatientData.MedicalData,
        dateTime: Instant,
    ): Boolean = patientMedicalDataDatabaseManager.updatePatientMedicalData(patientId, medicalData, dateTime)

    override fun getPatientMedicalData(
        patientId: PatientData.PatientId,
        from: Instant,
        to: Instant,
    ): List<Pair<Instant, PatientData.MedicalData>> =
        patientMedicalDataDatabaseManager.getPatientMedicalData(patientId, from, to)

    override fun getCurrentPatientMedicalData(patientId: PatientData.PatientId): PatientData.MedicalData? =
        patientMedicalDataDatabaseManager.getCurrentPatientMedicalData(patientId)

    override fun createPatient(patientId: PatientData.PatientId): Patient? =
        patientDigitalTwinManager.createPatientDT(patientId)

    override fun deletePatient(patientId: PatientData.PatientId): Boolean =
        patientDigitalTwinManager.deletePatientDT(patientId)

    override fun getPatientTaxCode(patientId: PatientData.PatientId): PatientData.TaxCode? =
        patientDigitalTwinManager.getPatientTaxCode(patientId)
}
