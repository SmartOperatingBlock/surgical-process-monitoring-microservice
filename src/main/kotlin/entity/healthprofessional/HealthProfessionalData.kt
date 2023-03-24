/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthprofessional

/** Module with all necessary data for [HealthProfessional]. */
object HealthProfessionalData {

    /** The [id] of health professionals. */
    data class HealthProfessionalId(val id: String) {
        init {
            require(this.id.isNotEmpty()) {
                "Health professional ID can not be empty!"
            }
        }
    }

    /** The types of health professionals roles. */
    enum class HealthProfessionalRole {
        NURSE, ANESTHETIST, SURGEON
    }
}
