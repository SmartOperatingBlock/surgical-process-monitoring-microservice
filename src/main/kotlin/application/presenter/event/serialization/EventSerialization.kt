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
import application.presenter.event.model.payloads.ProcessEventsPayloads.MedicalTechnologyUsage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/** Module that contains the logic for events serialization. */
object EventSerialization {

    /** Extension method to deserialize a json to an [Event]. */
    fun String.toEvent(eventKey: String): Event<*> = when (eventKey) {
        ProcessEventsKeys.MEDICAL_TECHNOLOGY_USAGE_EVENT ->
            Json.decodeFromString<ProcessEvent<MedicalTechnologyUsage>>(this)

        else -> throw IllegalArgumentException("Event not supported")
    }
}
