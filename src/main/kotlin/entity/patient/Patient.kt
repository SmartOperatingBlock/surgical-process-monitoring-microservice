/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.patient

import kotlinx.serialization.Serializable

/** The model of Patient composed by the [id], the [name],
 *  the [surname] and the [medicalData]. */
@Serializable
data class Patient(
    val id: PatientData.PatientId,
    val name: String? = null,
    val surname: String? = null,
    val medicalData: PatientData.MedicalData
) {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is Patient -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
