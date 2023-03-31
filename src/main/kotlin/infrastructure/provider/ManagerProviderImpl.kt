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

/** The implementation of the [ManagerProvider]. */
class ManagerProviderImpl : ManagerProvider {

    override val medicalDeviceDatabaseManager: MedicalDeviceDatabaseManager = TODO()
    override val medicalDeviceDigitalTwinManager: MedicalDeviceDigitalTwinManager = TODO()
    override val patientMedicalDataDatabaseManager: PatientMedicalDataDatabaseManager = TODO()
    override val processDatabaseManager: ProcessDatabaseManager = TODO()
    override val processDigitalTwinManager: ProcessDigitalTwinManager = TODO()
    override val surgeryBookingDigitalTwinManager: SurgeryBookingDigitalTwinManager = TODO()
}
