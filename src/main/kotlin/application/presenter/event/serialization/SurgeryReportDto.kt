/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.serialization

import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnology
import entity.patient.PatientData
import entity.process.ProcessData
import entity.report.SurgeryReport
import kotlinx.serialization.Serializable

/**
 * The DTO of th Report of the Surgical Process.
 * It includes:
 * - the [processId]
 * - the [processType]
 * - the [patientId]
 * - the [patientTaxCode]
 * - the [healthProfessionalId]
 * - the [preOperatingRoomId]
 * - the [operatingRoomId]
 * - the [processStates]
 * - the [processSteps]
 * - the [patientMedicalData]
 * - the [medicalDeviceUsage]
 * - the [medicalTechnologyUsage]
 */
@Serializable
data class SurgeryReportDto(
    val processId: String,
    val processType: String,
    val patientId: String,
    val patientTaxCode: String?,
    val healthProfessionalId: String?,
    val preOperatingRoomId: String?,
    val operatingRoomId: String?,
    val processStates: List<Pair<String, ProcessData.ProcessState>>,
    val processSteps: List<Pair<String, ProcessData.ProcessStep>>,
    val patientMedicalData: List<Pair<String, PatientData.MedicalData>>,
    val medicalDeviceUsage: List<ImplantableMedicalDevice>,
    val medicalTechnologyUsage: List<Pair<String, MedicalTechnology>>,
)

/**
 * Extension function to convert a [SurgeryReport] to its [SurgeryReportDto].
 */
fun SurgeryReport.toSurgeryReportDto() =
    SurgeryReportDto(
        this.processId.id,
        this.processType,
        this.patientId.id,
        this.patientTaxCode?.code,
        this.healthProfessionalId?.id,
        this.preOperatingRoom?.id?.id,
        this.operatingRoom?.id?.id,
        this.processStates.map { Pair(it.first.toString(), it.second) },
        this.processSteps.map { Pair(it.first.toString(), it.second) },
        this.patientMedicalData.map { Pair(it.first.toString(), it.second) },
        this.medicalDeviceUsage,
        this.medicalTechnologyUsage.map { Pair(it.first.toString(), it.second) },

    )
