/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model.payloads

import application.presenter.event.model.ProcessEvent
import entity.process.ProcessData
import kotlinx.serialization.Serializable

/** Module that wraps all payloads of process events. */
object ProcessEventsPayloads {

    /** Interface that identify a data payload that is accepted inside a [ProcessEvent]. */
    interface ProcessEventPayload

    /** Interface that identify a data payload that is accepted inside a [PatientData] event. */
    interface PatientDataPayload

    /**
     * The medical device used in the surgical process.
     * @param medicalDeviceID the id of the technology.
     * @param processId the id of the surgical process.
     */
    @Serializable
    data class MedicalDeviceUsage(val medicalDeviceID: String, val processId: String) : ProcessEventPayload

    /**
     * An information for the surgical process.
     * @param patientId the id of the Patient.
     */
    @Serializable
    data class PatientOnOperatingTable(val patientId: String) : ProcessEventPayload

    /**
     * The event of medical technology usage given its [medicalTechnologyID].
     * @param inUse true if the medical technology is in use, false otherwise.
     */
    @Serializable
    data class MedicalTechnologyUsage(val medicalTechnologyID: String, val inUse: Boolean) : ProcessEventPayload

    /**
     * The event of patient tracking inside the operating block.
     * @param patientId the id of the patient.
     * @param roomId the id of the room.
     * @param entered true if is entered in the room, false otherwise.
     * @param roomType the type of the room.
     */
    @Serializable
    data class PatientTracked(
        val patientId: String,
        val roomId: String,
        val entered: Boolean,
        val roomType: RoomType,
    ) : ProcessEventPayload

    /**
     * The [data] of the patient identified by [patientId].
     */
    @Serializable
    data class PatientData<E : PatientDataPayload>(val patientId: String, val data: E) : ProcessEventPayload

    /** The type of the room where the patient is tracked. **/
    enum class RoomType {
        PRE_OPERATING_ROOM, OPERATING_ROOM
    }

    /**
     * The body [temperature] of the patient.
     */
    @Serializable
    data class BodyTemperature(val temperature: Double) : PatientDataPayload

    /**
     * The [heartbeat] of the patient.
     */
    @Serializable
    data class Heartbeat(val heartbeat: Int) : PatientDataPayload

    /**
     * The diastolic [pressure] of the patient.
     */
    @Serializable
    data class DiastolicPressure(val pressure: Int) : PatientDataPayload

    /**
     * The systolic [pressure] of the patient.
     */
    @Serializable
    data class SystolicPressure(val pressure: Int) : PatientDataPayload

    /**
     * The [rate] of respiration the patient.
     */
    @Serializable
    data class RespiratoryRate(val rate: Int) : PatientDataPayload

    /**
     * The [saturation] of the patient oxygen.
     */
    @Serializable
    data class Saturation(val saturation: Int) : PatientDataPayload

    /**
     * The payload of an emergency surgery with the [roomId] and the optional [patientTaxCode].
     */
    data class EmergencySurgery(val roomId: String, val patientTaxCode: String?) : ProcessEventPayload

    /**
     * The payload of a step manual event with the [roomId] and the [step].
     */
    data class StepManualEvent(val roomId: String, val step: ProcessData.ProcessStep) : ProcessEventPayload
}
