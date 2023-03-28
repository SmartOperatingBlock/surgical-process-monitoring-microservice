/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.patient.PatientData
import usecase.repository.PatientRepository
import java.time.Instant

/**
 * Module that contains all the [ApplicationService] of Patient Medical Data.
 */
object PatientDataServices {

    /**
     * Application Service to update the patient medical data.
     */
    class UpdatePatientMedicalData(
        private val patientId: PatientData.PatientId,
        private val medicalData: PatientData.MedicalData,
        private val dateTime: Instant,
        private val patientRepository: PatientRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            patientRepository.updatePatientMedicalData(patientId, medicalData, dateTime)
    }

    /**
     * Application Service to get patient medical data in a given period.
     */
    class GetPatientMedicalData(
        private val patientId: PatientData.PatientId,
        private val from: Instant,
        private val to: Instant,
        private val patientRepository: PatientRepository
    ) : ApplicationService<Map<Instant, PatientData.MedicalData>> {
        override fun execute(): Map<Instant, PatientData.MedicalData> =
            patientRepository.getPatientMedicalData(
                patientId,
                from,
                to
            )
    }

    /**
     * Application Service to get the current patient medical data.
     */
    class GetCurrentPatientMedicalData(
        private val patientId: PatientData.PatientId,
        private val patientRepository: PatientRepository
    ) : ApplicationService<PatientData.MedicalData?> {
        override fun execute(): PatientData.MedicalData? =
            patientRepository.getCurrentPatientMedicalData(patientId)
    }
}
