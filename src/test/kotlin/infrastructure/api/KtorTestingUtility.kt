/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import infrastructure.api.routes.processAPI
import infrastructure.database.DatabaseManager
import infrastructure.database.withMongo
import infrastructure.digitaltwin.MockDigitalTwinManager
import infrastructure.provider.ManagerProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

object KtorTestingUtility {
    fun apiTestApplication(tests: suspend ApplicationTestBuilder.() -> Unit) {
        val apiPath = "/api/v1"
        val provider by lazy {
            object : ManagerProvider {
                private val digitalTwinManager = MockDigitalTwinManager()
                private val databaseManager = DatabaseManager("mongodb://localhost:27017")
                override val processDatabaseManager = databaseManager
                override val processDigitalTwinManager = digitalTwinManager
                override val patientMedicalDataDatabaseManager = databaseManager
                override val patientDigitalTwinManager = digitalTwinManager
                override val medicalDeviceDatabaseManager = databaseManager
                override val medicalDeviceDigitalTwinManager = digitalTwinManager
                override val surgeryBookingDigitalTwinManager = digitalTwinManager
            }
        }
        withMongo {
            testApplication {
                application {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                routing {
                    processAPI(apiPath, provider)
                }
                tests()
            }
        }
    }
}
