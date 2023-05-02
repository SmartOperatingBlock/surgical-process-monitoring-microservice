/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api

import entity.process.SurgicalProcess
import entity.room.Room
import kotlinx.serialization.Serializable

/**
 * The DTO for [SurgicalProcess] composed by:
 * - the [processId], [type], [patientId], [healthProfessionalId],
 * [preOperatingRoom], [operatingRoom], [state] and [step].
 */
@Serializable
data class SurgicalProcessApiDto(
    val processId: String,
    val type: String,
    val patientId: String,
    val healthProfessionalId: String?,
    val preOperatingRoom: Room?,
    val operatingRoom: Room?,
    val state: String,
    val step: String
)

/**
 * Extension function to convert a [SurgicalProcess] into its [SurgicalProcessApiDto].
 */
fun SurgicalProcess.toSurgicalProcessApiDto() =
    SurgicalProcessApiDto(
        this.id.id,
        this.type,
        this.patientId.id,
        this.healthProfessionalId?.id,
        this.preOperatingRoom,
        this.operatingRoom,
        this.state.toString(),
        this.step.toString()
    )
