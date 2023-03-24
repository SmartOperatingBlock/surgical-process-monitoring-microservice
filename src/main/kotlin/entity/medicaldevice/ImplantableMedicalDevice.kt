/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

/** The model of an Implantable Medical Device composed by the [id] and the [type]. */
data class ImplantableMedicalDevice(
    val id: MedicalDeviceData.ImplantableMedicalDeviceId,
    val type: MedicalDeviceData.DeviceType
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is ImplantableMedicalDevice -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
