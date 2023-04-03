/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

import application.presenter.event.model.payloads.ProcessEventsPayloads.ProcessEventPayload
import kotlinx.serialization.Serializable

/** The model of a Process Event composed by:
 *  - the [key]
 *  - the [data]
 *  - the [dateTime].
 */
@Serializable
data class ProcessEvent<E : ProcessEventPayload>(
    override val key: String,
    override val data: E,
    override val dateTime: String
) : Event<E>

/** Module that wrap all the possible keys for process events. */
object ProcessEventsKeys {
    /** The key of the medical device usage event. */
    const val MEDICAL_DEVICE_USAGE_EVENT = "MEDICAL_DEVICE_USAGE_EVENT"
    /** The key of the medical technology usage event. */
    const val MEDICAL_TECHNOLOGY_USAGE_EVENT = "MEDICAL_TECHNOLOGY_USAGE_EVENT"
    /** The key of the patient on operating bed event. */
    const val PATIENT_ON_OB_EVENT = "PATIENT_ON_OB_EVENT"
    /** The key of the body temperature update event. */
    const val PATIENT_BODY_TEMPERATURE_UPDATE_EVENT = "PATIENT_BODY_TEMPERATURE_UPDATE_EVENT"
    /** The key of the diastolic pressure update event. */
    const val PATIENT_DIASTOLIC_PRESSURE_UPDATE_EVENT = "PATIENT_DIASTOLIC_PRESSURE_UPDATE_EVENT"
    /** The key of the systolic pressure update event. */
    const val PATIENT_SYSTOLIC_PRESSURE_UPDATE_EVENT = "PATIENT_SYSTOLIC_PRESSURE_UPDATE_EVENT"
    /** The key of the respiratory rate update event. */
    const val PATIENT_RESPIRATORY_RATE_UPDATE_EVENT = "PATIENT_RESPIRATORY_RATE_UPDATE_EVENT"
    /** The key of the saturation update event. */
    const val PATIENT_SATURATION_UPDATE_EVENT = "PATIENT_SATURATION_UPDATE_EVENT"
    /** The key of the heartbeat update event. */
    const val PATIENT_HEARTBEAT_UPDATE_EVENT = "PATIENT_HEARTBEAT_UPDATE_EVENT"
    /** The key of the patient tracking event. */
    const val PATIENT_TRACKED_EVENT = "PATIENT_TRACKED_EVENT"
    /** The key of the event of an emergency surgery. */
    const val EMERGENCY_SURGERY_EVENT = "EMERGENCY_SURGERY_EVENT"
}
