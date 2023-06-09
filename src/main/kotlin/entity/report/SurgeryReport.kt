/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.report

import entity.healthprofessional.HealthProfessionalData
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnology
import entity.patient.PatientData
import entity.process.ProcessData
import entity.room.Room
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The Report of the Surgical Process.
 * It includes:
 * - the [processId]
 * - the [processType]
 * - the [patientId]
 * - the [patientTaxCode]
 * - the [healthProfessionalId]
 * - the [preOperatingRoom]
 * - the [operatingRoom]
 * - the [processStates]
 * - the [processSteps]
 * - the [patientMedicalData]
 * - the [medicalDeviceUsage]
 * - the [medicalTechnologyUsage]
 */
@Serializable
data class SurgeryReport(
    val processId: ProcessData.ProcessId,
    val processType: String,
    val patientId: PatientData.PatientId,
    val patientTaxCode: PatientData.TaxCode?,
    val healthProfessionalId: HealthProfessionalData.HealthProfessionalId?,
    val preOperatingRoom: Room?,
    val operatingRoom: Room?,
    val processStates: List<Pair<@Contextual Instant, ProcessData.ProcessState>>,
    val processSteps: List<Pair<@Contextual Instant, ProcessData.ProcessStep>>,
    val patientMedicalData: List<Pair<@Contextual Instant, PatientData.MedicalData>>,
    val medicalDeviceUsage: List<ImplantableMedicalDevice>,
    val medicalTechnologyUsage: List<Pair<@Contextual Instant, MedicalTechnology>>,
)
