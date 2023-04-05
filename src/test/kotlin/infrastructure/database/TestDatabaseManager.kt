/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.database

import entity.healthprofessional.HealthProfessional
import entity.healthprofessional.HealthProfessionalData
import entity.medicaldevice.MedicalDeviceData
import entity.patient.Patient
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import entity.room.RoomData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TestDatabaseManager : StringSpec({

    val processId = ProcessData.ProcessId("process1")
    val medicalTechnologyId = MedicalDeviceData.MedicalTechnologyId("medTech1")
    val medicalDeviceId = MedicalDeviceData.ImplantableMedicalDeviceId("medDev1")
    val patient = Patient(
        PatientData.PatientId("patient1"),
        "mario",
        "rossi",
        PatientData.MedicalData()
    )
    val healthProfessional = HealthProfessional(
        HealthProfessionalData.HealthProfessionalId("hp1"),
        "filippo",
        "bianchi",
    )
    val room = Room(
        RoomData.RoomId("room1"),
        null,
        RoomData.RoomType.OPERATING_ROOM
    )
    val surgicalProcess = SurgicalProcess(
        processId,
        Instant.now(),
        "operation",
        patient,
        healthProfessional,
        room,
        ProcessData.ProcessState.SURGERY,
        ProcessData.ProcessStep.ANESTHESIA
    )

    "test add medical technology usage" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            mongoClient.addMedicalTechnologyUsage(
                medicalTechnologyId,
                Instant.now(),
                processId,
                true
            ) shouldBe true
        }
    }

    "test add medical device usage" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            mongoClient.addMedicalDeviceUsage(
                medicalDeviceId,
                processId
            ) shouldBe true
        }
    }

    "test create surgical process" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            val process = mongoClient.createSurgicalProcess(surgicalProcess)
            mongoClient.getSurgicalProcessById(processId)?.id shouldBe process?.id
        }
    }

    "test update surgical process state" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            mongoClient.updateSurgicalProcessState(
                processId,
                Instant.now(),
                ProcessData.ProcessState.SURGERY
            ) shouldBe true
        }
    }
    "test update surgical process step" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            mongoClient.updateSurgicalProcessStep(
                processId,
                Instant.now(),
                ProcessData.ProcessStep.ANESTHESIA
            ) shouldBe true
        }
    }
})
