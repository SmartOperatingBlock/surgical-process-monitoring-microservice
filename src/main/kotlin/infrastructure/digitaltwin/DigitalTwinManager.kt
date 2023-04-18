/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin

import application.controller.manager.MedicalDeviceDigitalTwinManager
import application.controller.manager.PatientDigitalTwinManager
import application.controller.manager.ProcessDigitalTwinManager
import application.controller.manager.SurgeryBookingDigitalTwinManager
import com.azure.digitaltwins.core.BasicDigitalTwin
import com.azure.digitaltwins.core.BasicRelationship
import com.azure.digitaltwins.core.DigitalTwinsClient
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder
import com.azure.digitaltwins.core.implementation.models.ErrorResponseException
import com.azure.identity.DefaultAzureCredentialBuilder
import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.medicaldevice.MedicalDeviceData
import entity.patient.Patient
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import infrastructure.digitaltwin.presenter.SurgeryBookingAdt.toSurgeryBooking

/** The manager of Digital Twins Operations. */
class DigitalTwinManager :
    MedicalDeviceDigitalTwinManager,
    PatientDigitalTwinManager,
    ProcessDigitalTwinManager,
    SurgeryBookingDigitalTwinManager {

    init {
        checkNotNull(System.getenv(dtAppIdVariable)) { "azure client app id required" }
        checkNotNull(System.getenv(dtTenantVariable)) { "azure tenant id required" }
        checkNotNull(System.getenv(dtAppSecretVariable)) { "azure client secret id required" }
        checkNotNull(System.getenv(dtEndpointVariable)) { "azure dt endpoint required" }
    }

    private val dtClient = DigitalTwinsClientBuilder()
        .credential(DefaultAzureCredentialBuilder().build())
        .endpoint(System.getenv(dtEndpointVariable))
        .buildClient()

    override fun findSurgicalProcessByMedicalTechnology(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId
    ): SurgicalProcess? {
        TODO("Not yet implemented")
    }

    override fun createPatientDT(patientId: PatientData.PatientId): Patient? {
        TODO("Not yet implemented")
    }

    override fun createSurgicalProcess(process: SurgicalProcess): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateSurgicalProcessStep(processId: ProcessData.ProcessId, step: ProcessData.ProcessStep): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSurgeryBookingByPatient(patientId: PatientData.PatientId): SurgeryBooking? =
        this.dtClient.applySafeDigitalTwinOperation(null) {
            getDigitalTwin(patientId.id, BasicDigitalTwin::class.java).mapRelationships().toSurgeryBooking()
        }

    override fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean {
        TODO("Not yet implemented")
    }

    private fun <R> DigitalTwinsClient.applySafeDigitalTwinOperation(
        defaultResult: R,
        operation: DigitalTwinsClient.() -> R,
    ): R =
        try {
            operation()
        } catch (exception: ErrorResponseException) {
            println(exception)
            defaultResult
        }

    companion object {
        private const val dtAppIdVariable = "AZURE_CLIENT_ID"
        private const val dtTenantVariable = "AZURE_TENANT_ID"
        private const val dtAppSecretVariable = "AZURE_CLIENT_SECRET"
        private const val dtEndpointVariable = "AZURE_DT_ENDPOINT"
    }

    private fun BasicDigitalTwin.mapRelationships(): BasicDigitalTwin {
        dtClient.listRelationships(this.id, BasicRelationship::class.java).forEach {
            this.contents[it.name] = it.targetId
        }
        return this
    }
}
