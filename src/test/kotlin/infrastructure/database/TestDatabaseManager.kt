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
import java.time.temporal.ChronoUnit

class TestDatabaseManager : StringSpec({

    val processId = ProcessData.ProcessId("process1")
    val medicalTechnologyId = MedicalDeviceData.MedicalTechnologyId("medTech1")
    val medicalDeviceId = MedicalDeviceData.ImplantableMedicalDeviceId("medDev1")
    val patient = Patient(
        PatientData.PatientId("patient1"),
        "mario",
        "rossi",
        PatientData.MedicalData(),
    )
    val healthProfessional = HealthProfessional(
        HealthProfessionalData.HealthProfessionalId("hp1"),
        "filippo",
        "bianchi",
    )
    val room = Room(
        RoomData.RoomId("room1"),
        null,
        RoomData.RoomType.OPERATING_ROOM,
    )
    val surgicalProcess = SurgicalProcess(
        processId,
        Instant.now(),
        "operation",
        patient.id,
        healthProfessional.id,
        room,
        state = ProcessData.ProcessState.SURGERY,
        step = ProcessData.ProcessStep.ANESTHESIA,
    )

    val surgicalProcessTerminated = SurgicalProcess(
        processId,
        Instant.now(),
        "operation",
        patient.id,
        healthProfessional.id,
        room,
        state = ProcessData.ProcessState.TERMINATED,
        step = ProcessData.ProcessStep.ANESTHESIA,
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
                true,
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
                processId,
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
                ProcessData.ProcessState.SURGERY,
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
                ProcessData.ProcessStep.ANESTHESIA,
            ) shouldBe true
        }
    }

    "test get current surgical process step" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            mongoClient.createSurgicalProcess(surgicalProcess)
            mongoClient.createSurgicalProcess(surgicalProcessTerminated)
            mongoClient.getCurrentSurgicalProcesses().size shouldBe 1
        }
    }

    "test update patient medical data" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            val dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS)
            mongoClient.updatePatientMedicalData(
                patient.id,
                PatientData.MedicalData(PatientData.HeartBeat(40)),
                dateTime,
            )
            mongoClient.getPatientMedicalData(
                patient.id,
                dateTime,
                dateTime,
            ).first() shouldBe Pair(dateTime, PatientData.MedicalData(PatientData.HeartBeat(40)))
        }
    }

    "test get patient medical data with no medical data in the range" {
        withMongo {
            val mongoClient = DatabaseManager("mongodb://localhost:27017").also {
                it.database.drop()
            }
            val dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS)
            mongoClient.updatePatientMedicalData(
                patient.id,
                PatientData.MedicalData(saturationPercentage = PatientData.SaturationPercentage(24)),
                dateTime,
            )
            mongoClient.getPatientMedicalData(
                patient.id,
                dateTime.plusSeconds(10),
                dateTime.plusSeconds(20),
            ).size shouldBe 0
        }
    }
})
