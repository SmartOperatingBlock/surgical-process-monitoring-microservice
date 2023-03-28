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
import application.controller.manager.PatientMedicalDataDatabaseManager
import application.controller.manager.ProcessDatabaseManager
import application.controller.manager.ProcessDigitalTwinManager
import application.controller.manager.SurgeryBookingDigitalTwinManager

/**
 * The provider of Database and Digital Twins Managers.
 */
interface ManagerProvider {

    /** The manager of the database of surgical processes. */
    val processDatabaseManager: ProcessDatabaseManager

    /** The manager of the Digital TWins of surgical processes. */
    val processDigitalTwinManager: ProcessDigitalTwinManager

    /** The manager of the database of patient medical data. */
    val patientMedicalDataDatabaseManager: PatientMedicalDataDatabaseManager

    /** The manager of the database of medical device usage. */
    val medicalDeviceDatabaseManager: MedicalDeviceDatabaseManager

    /** The manager of the medical devices digital twins. */
    val medicalDeviceDigitalTwinManager: MedicalDeviceDigitalTwinManager

    /** The manager of the surgery booking digital twins. */
    val surgeryBookingDigitalTwinManager: SurgeryBookingDigitalTwinManager
}
