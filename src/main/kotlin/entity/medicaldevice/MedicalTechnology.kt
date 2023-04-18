/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

/**
 * The model of a Medical Technology composed by the [id], the [name],
 * the [description], the [type] and a value [inUse] to check if the technology is in use.
 */
data class MedicalTechnology(
    val id: MedicalDeviceData.MedicalTechnologyId,
    val name: String,
    val description: String? = null,
    val type: MedicalDeviceData.MedicalTechnologyType,
    val inUse: Boolean,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is MedicalTechnology -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
