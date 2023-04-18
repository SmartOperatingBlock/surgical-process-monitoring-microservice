/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.booking

import entity.healthprofessional.HealthProfessional
import entity.patient.Patient
import java.time.Instant

/** The model of a Surgery booking composed by the [id], the [dateTime],
 * the [healthProfessional], the [patient] and the [surgeryType].
 */
data class SurgeryBooking(
    val id: SurgeryBookingData.SurgeryBookingId,
    val dateTime: Instant,
    val healthProfessional: HealthProfessional,
    val patient: Patient,
    val surgeryType: String,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgeryBooking -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
