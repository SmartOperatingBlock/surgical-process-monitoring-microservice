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
import com.azure.core.models.JsonPatchDocument
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
import entity.room.Room
import entity.room.RoomData
import infrastructure.digitaltwin.presenter.PatientAdt.toDigitalTwin
import infrastructure.digitaltwin.presenter.SurgeryBookingAdt
import infrastructure.digitaltwin.presenter.SurgeryBookingAdt.toSurgeryBooking
import infrastructure.digitaltwin.presenter.SurgicalProcessAdt
import infrastructure.digitaltwin.presenter.SurgicalProcessAdt.toDigitalTwin
import infrastructure.digitaltwin.query.AdtQuery
import infrastructure.digitaltwin.query.AdtQuery.Companion.AdtQueryUtils.eq
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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
    ): ProcessData.ProcessId? =
        this.dtClient.applySafeDigitalTwinOperation(null) {
            val roomId = query(
                AdtQuery
                    .createQuery()
                    .selectTop(1, "CT.\$dtId")
                    .fromDigitalTwins("T")
                    .joinRelationship(
                        "CT",
                        "T",
                        "rel_is_located",
                    ).where("T.\$dtId" eq medicalTechnologyId.id).query,
                String::class.java,
            ).let {
                if (it.count() == 1) {
                    Json.parseToJsonElement(it.first()).jsonObject["\$dtId"]?.let { id ->
                        RoomData.RoomId(id.jsonPrimitive.content)
                    }
                } else {
                    null
                }
            }
            roomId?.let {
                query(
                    AdtQuery.createQuery()
                        .selectTop(1, "Process.\$dtId")
                        .fromDigitalTwins("Process")
                        .joinRelationship("Room", "Process", SurgicalProcessAdt.ROOM_RELATIONSHIP)
                        .where("Room.\$dtId" eq it.id).query,
                    String::class.java,
                ).let { result ->
                    if (result.count() == 1) {
                        Json.parseToJsonElement(result.first()).jsonObject["\$dtId"]?.let { id ->
                            ProcessData.ProcessId(id.jsonPrimitive.content)
                        }
                    } else {
                        null
                    }
                }
            }
        }

    override fun createPatientDT(patientId: PatientData.PatientId): Patient? =
        with(Patient(patientId, medicalData = PatientData.MedicalData()).toDigitalTwin()) {
            dtClient.applySafeDigitalTwinOperation(null) {
                createOrReplaceDigitalTwin(this@with.id, this@with, BasicDigitalTwin::class.java)
                Patient(patientId, medicalData = PatientData.MedicalData())
            }
        }

    override fun createSurgicalProcess(process: SurgicalProcess): Boolean =
        with(process.toDigitalTwin()) {
            dtClient.applySafeDigitalTwinOperation(false) {
                createOrReplaceDigitalTwin(this@with.id, this@with, BasicDigitalTwin::class.java)
                val booking = getSurgeryBookingByPatient(process.patientId)
                booking?.let {
                    createOrReplaceRelationship(
                        process.id.id,
                        "${process.id.id}-${it.id.id}",
                        BasicRelationship(
                            "${process.id.id}-${it.id.id}",
                            process.id.id,
                            it.id.id,
                            SurgicalProcessAdt.BOOKING_RELATIONSHIP
                        ),
                        BasicRelationship::class.java
                    )
                    createOrReplaceRelationship(
                        process.id.id,
                        "${process.id.id}-${it.patientId.id}",
                        BasicRelationship(
                            "${process.id.id}-${it.patientId.id}",
                            process.id.id,
                            it.patientId.id,
                            SurgicalProcessAdt.PATIENT_RELATIONSHIP
                        ),
                        BasicRelationship::class.java
                    )
                    process.preOperatingRoom?.let { room -> updateSurgicalProcessRoom(process.id, room) }
                    process.operatingRoom?.let { room -> updateSurgicalProcessRoom(process.id, room) }
                }
                true
            }
        }

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean =
        this.dtClient.applySafeDigitalTwinOperation(false) {
            updateDigitalTwin(processId.id, JsonPatchDocument().appendAdd("process_state", state.ordinal))
            true
        }

    override fun updateSurgicalProcessStep(processId: ProcessData.ProcessId, step: ProcessData.ProcessStep): Boolean =
        this.dtClient.applySafeDigitalTwinOperation(false) {
            updateDigitalTwin(processId.id, JsonPatchDocument().appendAdd("process_step", step.ordinal))
            true
        }

    override fun updateSurgicalProcessRoom(processId: ProcessData.ProcessId, room: Room): Boolean =
        this.dtClient.applySafeDigitalTwinOperation(false) {
            this.listRelationships(processId.id, BasicRelationship::class.java).forEach {
                if (it.targetId == room.id.id) {
                    this.deleteRelationship(it.sourceId, it.id)
                }
            }.let {
                this.createOrReplaceRelationship(
                    processId.id,
                    "${processId.id}-${room.id.id}",
                    BasicRelationship(
                        "${processId.id}-${room.id.id}",
                        processId.id,
                        room.id.id,
                        SurgicalProcessAdt.ROOM_RELATIONSHIP
                    ),
                    BasicRelationship::class.java
                )
            }
            true
        }

    override fun getSurgeryBookingByPatient(patientId: PatientData.PatientId): SurgeryBooking? =
        this.dtClient.applySafeDigitalTwinOperation(null) {
            val bookingId: String? = query(
                AdtQuery.createQuery()
                    .selectTop(1, "T.\$dtId")
                    .fromDigitalTwins("T")
                    .joinRelationship("CT", "T", SurgeryBookingAdt.PATIENT_RELATIONSHIP)
                    .where("CT.\$dtId" eq patientId.id).query,
                String::class.java
            ).run {
                if (this.count() == 1) {
                    Json.parseToJsonElement(this.first()).jsonObject["\$dtId"]?.jsonPrimitive?.content
                } else {
                    null
                }
            }
            bookingId?.let {
                getDigitalTwin(bookingId, BasicDigitalTwin::class.java)
                    .mapRelationships()
                    .toSurgeryBooking()
            }
        }

    override fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean =
        this.dtClient.applySafeDigitalTwinOperation(false) {
            this.listRelationships(surgeryBookingId.id, BasicRelationship::class.java).firstOrNull {
                it.targetId == patientId.id
            }?.let {
                this.deleteRelationship(surgeryBookingId.id, it.id)
                true
            }
            false
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

    /** Utility method to insert target relationships to the map with digital twins properties. */
    fun BasicDigitalTwin.mapRelationships(): BasicDigitalTwin {
        dtClient.listRelationships(this.id, BasicRelationship::class.java).forEach {
            this.contents[it.name] = it.targetId
        }
        return this
    }
}
