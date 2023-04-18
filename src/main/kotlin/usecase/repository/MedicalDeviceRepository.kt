/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import java.time.Instant

/**
 * Interface that models the repository to manage the medical device.
 */
interface MedicalDeviceRepository {

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
        processId: ProcessData.ProcessId,
        dateTime: Instant,
        inUse: Boolean
    ): Boolean

    /**
     * Find a surgical process by a [medicalTechnologyId].
     * @return the surgical process.
     */
    fun findSurgicalProcessByMedicalTechnology(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId
    ): ProcessData.ProcessId?
}
