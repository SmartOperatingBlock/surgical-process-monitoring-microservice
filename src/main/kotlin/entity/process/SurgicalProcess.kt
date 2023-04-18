/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import entity.healthprofessional.HealthProfessional
import entity.patient.Patient
import entity.room.Room
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/** The model of a Surgical Process composed by:
 * - the [id] of the process
 * - the [dateTime] of the process
 * - the [type] of the surgery
 * - the [patient] that is going to be operated
 * - the [healthProfessional] in charge of the process
 * - the [room] in which the process is done
 * - the current [state] of the process
 * - the current [step] of the process.
 */
@Serializable
data class SurgicalProcess(
    val id: ProcessData.ProcessId,
    @Contextual
    val dateTime: Instant,
    val type: String,
    val patient: Patient?,
    val healthProfessional: HealthProfessional?,
    val room: Room,
    val state: ProcessData.ProcessState,
    val step: ProcessData.ProcessStep? = null,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgicalProcess -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
