/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.room

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestRoom : StringSpec({

    "Room Id should not be empty" {
        shouldThrow<IllegalArgumentException> {
            Room(RoomData.RoomId(""), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM)
        }
    }

    "Room should be equal to other room with same id" {
        val id = RoomData.RoomId("room-1")
        Room(id, type = RoomData.RoomType.OPERATING_ROOM) shouldBe Room(id, type = RoomData.RoomType.OPERATING_ROOM)
    }
})
