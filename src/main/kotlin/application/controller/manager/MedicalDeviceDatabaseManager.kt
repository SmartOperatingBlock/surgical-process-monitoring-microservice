/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import java.time.Instant

/**
 * This interface models the manager of the database for medical devices.
 */
interface MedicalDeviceDatabaseManager {

    /**
     * Add new usage of an implantable medical device.
     * @return true if correctly added, false otherwise.
     */
    fun addMedicalDeviceUsage(
        medicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        processId: ProcessData.ProcessId
    ): Boolean

    /**
     * Add new usage of a medical technology.
     * @return true if correctly created, false otherwise.
     */
    fun addMedicalTechnologyUsage(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        dateTime: Instant,
        processId: ProcessData.ProcessId,
        inUse: Boolean
    ): Boolean

    /**
     * Get the usage of implantable medical devices in a process.
     */
    fun getMedicalDeviceUsageByProcessId(
        processId: ProcessData.ProcessId
    ): List<MedicalDeviceData.ImplantableMedicalDeviceId>
}
