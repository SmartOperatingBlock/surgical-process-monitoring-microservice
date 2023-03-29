/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.room

import kotlinx.serialization.Serializable

/** The model of a Room of the Operating Block composed by:
 * - the [id] of the room,
 * - the [name] of the room,
 * - the [type] of the room.
 */
@Serializable
data class Room(
    val id: RoomData.RoomId,
    val name: String? = null,
    val type: RoomData.RoomType
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is Room -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}
