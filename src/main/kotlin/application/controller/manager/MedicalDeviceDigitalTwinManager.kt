/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalDeviceData
import entity.medicaldevice.MedicalTechnology
import entity.process.ProcessData

/** This interface model the operation on Medical Devices Digital Twins. */
interface MedicalDeviceDigitalTwinManager {

    /**
     * Find the surgical process in which a medical technology is being used.
     * @param medicalTechnologyId the id of the medical technology.
     */
    fun findSurgicalProcessByMedicalTechnology(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
    ): ProcessData.ProcessId?

    /**
     * Get the [ImplantableMedicalDevice] by its [implantableMedicalDeviceId].
     */
    fun getMedicalDeviceById(
        implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
    ): ImplantableMedicalDevice?

    /**
     * Get the [MedicalTechnology] by its [medicalTechnologyId] and set the value if it is [inUse].
     */
    fun getMedicalTechnologyById(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        inUse: Boolean,
    ): MedicalTechnology?

    /**
     * Delete the [ImplantableMedicalDevice] by its [implantableMedicalDeviceId].
     */
    fun deleteMedicalDevice(implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId): Boolean
}
