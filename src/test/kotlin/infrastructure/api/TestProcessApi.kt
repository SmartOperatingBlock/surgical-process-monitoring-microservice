/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import application.controller.SurgicalProcessController
import application.service.SurgicalProcessServices
import entity.healthprofessional.HealthProfessionalData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import entity.room.RoomData
import infrastructure.api.KtorTestingUtility.apiTestApplication
import infrastructure.database.DatabaseManager
import infrastructure.digitaltwin.MockDigitalTwinManager
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.Instant

class TestProcessApi : StringSpec({

    fun ApplicationTestBuilder.createSampleSurgicalProcess() =
        SurgicalProcessServices.CreateSurgicalProcess(
            SurgicalProcess(
                ProcessData.ProcessId("process-0"),
                Instant.now(),
                "Surgery-1",
                PatientData.PatientId("patient-0"),
                HealthProfessionalData.HealthProfessionalId("hp-0"),
                Room(RoomData.RoomId("room-1"), type = RoomData.RoomType.PRE_POST_OPERATING_ROOM),
                state = ProcessData.ProcessState.PRE_SURGERY,
            ),
            SurgicalProcessController(DatabaseManager("mongodb://localhost:27017"), MockDigitalTwinManager())
        ).execute()

    "When there are no processes the response should be no content" {
        apiTestApplication {
            val response = client.get("/api/v1/processes")
            response shouldHaveStatus HttpStatusCode.NoContent
        }
    }

    "It should be possible to obtain current surgical processes" {
        apiTestApplication {
            createSampleSurgicalProcess() shouldNotBe null
            val response = client.get("/api/v1/processes")
            response shouldHaveStatus HttpStatusCode.OK
        }
    }
})
