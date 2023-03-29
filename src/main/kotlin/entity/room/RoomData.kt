/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.room

import kotlinx.serialization.Serializable

/** Module with all data necessary for Operating Block [Room]. */
object RoomData {

    /** The [id] of a [Room]. */
    @Serializable
    data class RoomId(val id: String) {
        init {
            require(this.id.isNotEmpty()) {
                "Room ID can not be empty!"
            }
        }
    }

    /** The possible type of [Room]. */
    enum class RoomType {
        PRE_POST_OPERATING_ROOM, OPERATING_ROOM
    }
}
