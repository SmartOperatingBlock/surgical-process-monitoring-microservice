/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import entity.healthprofessional.HealthProfessional
import entity.healthprofessional.HealthProfessionalData
import entity.patient.Patient
import entity.patient.PatientData
import entity.room.Room
import entity.room.RoomData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TestProcess : StringSpec({

    val healthProfessional = HealthProfessional(HealthProfessionalData.HealthProfessionalId("12345678"))
    val patient = Patient(PatientData.PatientId("01"), medicalData = PatientData.MedicalData())

    "Surgical Process should not have an empty id" {
        shouldThrow<IllegalArgumentException> {
            SurgicalProcess(
                ProcessData.ProcessId(""),
                Instant.now(),
                "Surgery-1",
                patient.id,
                healthProfessional.id,
                operatingRoom = Room(RoomData.RoomId("room-1"), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM),
                state = ProcessData.ProcessState.PRE_SURGERY,
            )
        }
    }

    "Surgical Process should be equal to other process with same id" {
        val id = ProcessData.ProcessId("sp-1")
        val first = SurgicalProcess(
            id,
            Instant.now(),
            "Surgery-1",
            patient.id,
            healthProfessional.id,
            operatingRoom = Room(RoomData.RoomId("room-1"), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM),
            state = ProcessData.ProcessState.PRE_SURGERY,
        )
        val second = SurgicalProcess(
            id,
            Instant.now(),
            "Surgery-1",
            patient.id,
            healthProfessional.id,
            Room(RoomData.RoomId("room-1"), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM),
            state = ProcessData.ProcessState.PRE_SURGERY,
        )
        first shouldBe second
    }
})
