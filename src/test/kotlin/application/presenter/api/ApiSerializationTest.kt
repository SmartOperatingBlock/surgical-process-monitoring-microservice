/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api

import entity.healthprofessional.HealthProfessionalData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import entity.room.RoomData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import java.time.Instant

class ApiSerializationTest : StringSpec({

    val sampleSurgicalProcess = SurgicalProcess(
        ProcessData.ProcessId("sample-process"),
        Instant.now(),
        "Surgical-process-type",
        PatientData.PatientId("sample-patient"),
        HealthProfessionalData.HealthProfessionalId("sample-health-professional"),
        Room(RoomData.RoomId("sample-pre-or"), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM),
        Room(RoomData.RoomId("sample-or"), type = RoomData.RoomType.OPERATING_ROOM),
        ProcessData.ProcessState.SURGERY,
        ProcessData.ProcessStep.PATIENT_ON_OPERATING_TABLE
    )

    val sampleSurgicalProcessDTO = SurgicalProcessApiDto(
        sampleSurgicalProcess.id.id,
        sampleSurgicalProcess.type,
        sampleSurgicalProcess.patientId.id,
        sampleSurgicalProcess.healthProfessionalId?.id,
        sampleSurgicalProcess.preOperatingRoom,
        sampleSurgicalProcess.operatingRoom,
        sampleSurgicalProcess.state.toString(),
        sampleSurgicalProcess.step.toString()
    )

    "It should be possible to obtain the right DTO of a surgical process" {
        sampleSurgicalProcess.toSurgicalProcessApiDto() shouldBeEqualToComparingFields sampleSurgicalProcessDTO
    }
})
