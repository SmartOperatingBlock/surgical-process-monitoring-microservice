/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalDeviceData
import entity.medicaldevice.MedicalTechnology
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

    /**
     * Get the medical devices used in a surgical process by the [processId].
     */
    fun getMedicalDeviceUsageByProcessId(
        processId: ProcessData.ProcessId
    ): List<MedicalDeviceData.ImplantableMedicalDeviceId>

    /**
     * Get the [ImplantableMedicalDevice] by its [implantableMedicalDeviceId].
     */
    fun getMedicalDeviceById(
        implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId
    ): ImplantableMedicalDevice?

    /**
     * Get the medical technologies used in a surgical process by the [processId].
     */
    fun getMedicalTechnologyUsageByProcessId(
        processId: ProcessData.ProcessId
    ): List<Triple<Instant, MedicalDeviceData.MedicalTechnologyId, Boolean>>

    /**
     * Get the [MedicalTechnology] by its [medicalTechnologyId] and set the value if it is [inUse].
     */
    fun getMedicalTechnologyById(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        inUse: Boolean
    ): MedicalTechnology?

    /**
     * Delete the [ImplantableMedicalDevice] by its [implantableMedicalDeviceId].
     */
    fun deleteMedicalDevice(implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId): Boolean
}
