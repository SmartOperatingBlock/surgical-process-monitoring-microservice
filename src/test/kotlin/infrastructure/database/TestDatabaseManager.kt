/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.database

import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TestDatabaseManager : StringSpec({

    val processId = ProcessData.ProcessId("process1")
    val medicalTechnologyId = MedicalDeviceData.MedicalTechnologyId("medTech1")
    val medicalDeviceId = MedicalDeviceData.ImplantableMedicalDeviceId("medDev1")

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
})
