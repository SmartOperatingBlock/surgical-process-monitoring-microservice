/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller

import application.controller.manager.MedicalDeviceDatabaseManager
import application.controller.manager.MedicalDeviceDigitalTwinManager
import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import usecase.repository.MedicalDeviceRepository
import java.time.Instant

/**
 * The implementation of the [MedicalDeviceRepository].
 */
class MedicalDeviceController(
    private val medicalDeviceDatabaseManager: MedicalDeviceDatabaseManager,
    private val medicalDeviceDigitalTwinManager: MedicalDeviceDigitalTwinManager
) : MedicalDeviceRepository {

    override fun addMedicalDeviceUsage(
        medicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        processId: ProcessData.ProcessId
    ): Boolean = this.medicalDeviceDatabaseManager.addMedicalDeviceUsage(medicalDeviceId, processId)

    override fun addMedicalTechnologyUsage(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        processId: ProcessData.ProcessId,
        dateTime: Instant,
        inUse: Boolean
    ): Boolean = this.addMedicalTechnologyUsage(medicalTechnologyId, processId, dateTime, inUse)

    override fun findSurgicalProcessByMedicalTechnology(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId
    ): ProcessData.ProcessId? =
        this.medicalDeviceDigitalTwinManager.findSurgicalProcessByMedicalTechnology(medicalTechnologyId)
}
