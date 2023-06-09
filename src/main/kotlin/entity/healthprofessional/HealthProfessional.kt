/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthprofessional

import kotlinx.serialization.Serializable

/** The model of Health Professionals composed by the [id], the [name],
 *  the [surname] and the [role]. */
@Serializable
data class HealthProfessional(
    val id: HealthProfessionalData.HealthProfessionalId,
    val name: String? = null,
    val surname: String? = null,
    val role: HealthProfessionalData.HealthProfessionalRole = HealthProfessionalData.HealthProfessionalRole.SURGEON,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is HealthProfessional -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
