/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.serialization

import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.ProcessEventsKeys
import application.presenter.event.model.payloads.ProcessEventsPayloads
import application.presenter.event.model.payloads.ProcessEventsPayloads.BodyTemperature
import application.presenter.event.model.payloads.ProcessEventsPayloads.DiastolicPressure
import application.presenter.event.model.payloads.ProcessEventsPayloads.EmergencySurgery
import application.presenter.event.model.payloads.ProcessEventsPayloads.Heartbeat
import application.presenter.event.model.payloads.ProcessEventsPayloads.MedicalDeviceUsage
import application.presenter.event.model.payloads.ProcessEventsPayloads.MedicalTechnologyUsage
import application.presenter.event.model.payloads.ProcessEventsPayloads.PatientData
import application.presenter.event.model.payloads.ProcessEventsPayloads.PatientOnOperatingTable
import application.presenter.event.model.payloads.ProcessEventsPayloads.PatientTracked
import application.presenter.event.model.payloads.ProcessEventsPayloads.ProcessEventPayload
import application.presenter.event.model.payloads.ProcessEventsPayloads.RespiratoryRate
import application.presenter.event.model.payloads.ProcessEventsPayloads.Saturation
import application.presenter.event.model.payloads.ProcessEventsPayloads.SystolicPressure
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/** Module that contains the logic for events serialization. */
object EventSerialization {

    /** Extension method to deserialize a json to an [Event]. */
    fun String.toEvent(eventKey: String): Event<*> = when (eventKey) {
        ProcessEventsKeys.MEDICAL_TECHNOLOGY_USAGE_EVENT ->
            deserializeToEvent<MedicalTechnologyUsage>(this)
        ProcessEventsKeys.MEDICAL_DEVICE_USAGE_EVENT ->
            deserializeToEvent<MedicalDeviceUsage>(this)
        ProcessEventsKeys.PATIENT_ON_OB_EVENT ->
            deserializeToEvent<PatientOnOperatingTable>(this)
        ProcessEventsKeys.PATIENT_TRACKED_EVENT ->
            deserializeToEvent<PatientTracked>(this)
        ProcessEventsKeys.PATIENT_BODY_TEMPERATURE_UPDATE_EVENT ->
            deserializeToEvent<PatientData<BodyTemperature>>(this)
        ProcessEventsKeys.PATIENT_DIASTOLIC_PRESSURE_UPDATE_EVENT ->
            deserializeToEvent<PatientData<DiastolicPressure>>(this)
        ProcessEventsKeys.PATIENT_SYSTOLIC_PRESSURE_UPDATE_EVENT ->
            deserializeToEvent<PatientData<SystolicPressure>>(this)
        ProcessEventsKeys.PATIENT_RESPIRATORY_RATE_UPDATE_EVENT ->
            deserializeToEvent<PatientData<RespiratoryRate>>(this)
        ProcessEventsKeys.PATIENT_SATURATION_UPDATE_EVENT ->
            deserializeToEvent<PatientData<Saturation>>(this)
        ProcessEventsKeys.PATIENT_HEARTBEAT_UPDATE_EVENT ->
            deserializeToEvent<PatientData<Heartbeat>>(this)
        ProcessEventsKeys.EMERGENCY_SURGERY_EVENT ->
            deserializeToEvent<EmergencySurgery>(this)
        ProcessEventsKeys.STEP_MANUAL_EVENT ->
            deserializeToEvent<ProcessEventsPayloads.StepManualEvent>(this)
        else -> throw IllegalArgumentException("Event not supported")
    }

    private inline fun <reified X : ProcessEventPayload> deserializeToEvent(event: String) =
        Json.decodeFromString<ProcessEvent<X>>(event)
}
