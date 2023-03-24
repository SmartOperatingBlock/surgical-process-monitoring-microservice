/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

/** Module with all data necessary for [ImplantableMedicalDevice]. */
object MedicalDeviceData {

    /** The [id] of an [ImplantableMedicalDevice]. */
    data class ImplantableMedicalDeviceId(val id: String) {
        init {
            require(id.isNotEmpty()) {
                "Implantable medical device id cannot be empty!"
            }
        }
    }

    /** The [id] of a [MedicalTechnology]. */
    data class MedicalTechnologyId(val id: String) {
        init {
            require(id.isNotEmpty()) {
                "Medical Technology id cannot be empty!"
            }
        }
    }

    /** The type of [ImplantableMedicalDevice]. */
    enum class DeviceType {
        CATHETER, PACE_MAKER
    }

    /** The type of [MedicalTechnology]. */
    enum class MedicalTechnologyType {
        ENDOSCOPE, X_RAY
    }
}
