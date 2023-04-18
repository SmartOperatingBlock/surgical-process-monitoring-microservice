/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.provider

import application.controller.manager.MedicalDeviceDatabaseManager
import application.controller.manager.MedicalDeviceDigitalTwinManager
import application.controller.manager.PatientDigitalTwinManager
import application.controller.manager.PatientMedicalDataDatabaseManager
import application.controller.manager.ProcessDatabaseManager
import application.controller.manager.ProcessDigitalTwinManager
import application.controller.manager.SurgeryBookingDigitalTwinManager
import infrastructure.database.DatabaseManager
import infrastructure.digitaltwin.DigitalTwinManager

/** The implementation of the [ManagerProvider]. */
class ManagerProviderImpl : ManagerProvider {
    init {
        checkNotNull(System.getenv("SURGICAL_PROCESS_MONGODB_URL")) {
            "Please provide the surgical process monitoring system MongoDB connection string!"
        }
    }
    private val databaseManager = DatabaseManager(System.getenv("SURGICAL_PROCESS_MONGODB_URL"))
    private val digitalTwinManager = DigitalTwinManager()
    override val medicalDeviceDatabaseManager: MedicalDeviceDatabaseManager = databaseManager
    override val medicalDeviceDigitalTwinManager: MedicalDeviceDigitalTwinManager = digitalTwinManager
    override val patientMedicalDataDatabaseManager: PatientMedicalDataDatabaseManager = databaseManager
    override val patientDigitalTwinManager: PatientDigitalTwinManager = digitalTwinManager
    override val processDatabaseManager: ProcessDatabaseManager = databaseManager
    override val processDigitalTwinManager: ProcessDigitalTwinManager = digitalTwinManager
    override val surgeryBookingDigitalTwinManager: SurgeryBookingDigitalTwinManager = digitalTwinManager
}
