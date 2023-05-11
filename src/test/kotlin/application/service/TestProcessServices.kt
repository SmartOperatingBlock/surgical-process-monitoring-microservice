/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import application.controller.SurgicalProcessController
import entity.healthprofessional.HealthProfessionalData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import entity.room.RoomData.RoomId
import entity.room.RoomData.RoomType
import infrastructure.database.DatabaseManager
import infrastructure.database.withMongo
import infrastructure.digitaltwin.MockDigitalTwinManager
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import java.time.Instant

class TestProcessServices : StringSpec({

    fun controller() = SurgicalProcessController(
        DatabaseManager("mongodb://localhost:27017"),
        MockDigitalTwinManager(),
    )

    val sampleSurgicalProcess = SurgicalProcess(
        ProcessData.ProcessId("sample-process"),
        Instant.now(),
        "Surgical-process-type",
        PatientData.PatientId("sample-patient"),
        HealthProfessionalData.HealthProfessionalId("sample-health-professional"),
        Room(RoomId("sample-pre-or"), type = RoomType.PRE_POST_OPERATING_ROOM),
        Room(RoomId("sample-or"), type = RoomType.OPERATING_ROOM),
        ProcessData.ProcessState.SURGERY,
        ProcessData.ProcessStep.PATIENT_ON_OPERATING_TABLE,
    )

    "It should be possible to create a surgical process" {
        withMongo {
            val controller = controller()
            SurgicalProcessServices.CreateSurgicalProcess(
                sampleSurgicalProcess,
                controller,
            ).execute().run {
                controller.getCurrentSurgicalProcesses().size shouldBeGreaterThan 0
            }
        }
    }

    "It should be possible to retrieve a surgical process by its id" {
        withMongo {
            val controller = controller()
            SurgicalProcessServices.CreateSurgicalProcess(
                sampleSurgicalProcess,
                controller,
            ).execute()?.let {
                SurgicalProcessServices.GetSurgicalProcessById(it.id, controller).execute().run {
                    this shouldNotBe null
                }
            }
        }
    }

    "It should not be possible to delete a surgical process in the database" {
        withMongo {
            val controller = controller()
            SurgicalProcessServices.CreateSurgicalProcess(
                sampleSurgicalProcess,
                controller,
            ).execute()?.let {
                SurgicalProcessServices.DeleteSurgicalProcess(it.id, controller).execute().run {
                    controller.getSurgicalProcessById(sampleSurgicalProcess.id) shouldNotBe null
                }
            }
        }
    }
})
