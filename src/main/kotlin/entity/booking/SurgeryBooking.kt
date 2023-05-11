/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.booking

import entity.healthprofessional.HealthProfessionalData
import entity.patient.PatientData
import java.time.Instant

/** The model of a Surgery booking composed by the [id], the [dateTime],
 * the [healthProfessionalId], the [patientId] and the [surgeryType].
 */
data class SurgeryBooking(
    val id: SurgeryBookingData.SurgeryBookingId,
    val dateTime: Instant,
    val healthProfessionalId: HealthProfessionalData.HealthProfessionalId,
    val patientId: PatientData.PatientId,
    val surgeryType: String,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgeryBooking -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
