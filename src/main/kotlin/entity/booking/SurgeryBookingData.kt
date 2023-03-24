/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.booking

/** Module with all data necessary for [SurgeryBooking]. */
object SurgeryBookingData {

    /** The [id] of a [SurgeryBooking]. */
    data class SurgeryBookingId(val id: String) {
        init {
            require(id.isNotEmpty()) {
                "Surgery booking id cannot be empty!"
            }
        }
    }
}
