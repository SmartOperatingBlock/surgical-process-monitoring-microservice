/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import usecase.repository.MedicalDeviceRepository
import java.time.Instant

/**
 * Module that wraps all the application services of medical devices.
 */
object MedicalDeviceServices {

    /**
     * The [ApplicationService] to add the usage of a medical device.
     */
    class AddMedicalDeviceUsage(
        private val medicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        private val processId: ProcessData.ProcessId,
        private val medicalDeviceRepository: MedicalDeviceRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean = this.medicalDeviceRepository.addMedicalDeviceUsage(medicalDeviceId, processId)
    }

    /**
     * The [ApplicationService] to add the usage of a medical technology.
     */
    class AddMedicalTechnologyUsage(
        private val medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        private val processId: ProcessData.ProcessId,
        private val dateTime: Instant,
        private val inUse: Boolean,
        private val medicalDeviceRepository: MedicalDeviceRepository
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean = this.medicalDeviceRepository.addMedicalTechnologyUsage(
            medicalTechnologyId,
            processId,
            dateTime,
            inUse
        )
    }

    /**
     * The [ApplicationService] to find a surgical process by a medical technology.
     */
    class FindProcessByMedicalTechnology(
        private val medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        private val medicalDeviceRepository: MedicalDeviceRepository
    ) : ApplicationService<ProcessData.ProcessId?> {

        override fun execute(): ProcessData.ProcessId? =
            this.medicalDeviceRepository.findSurgicalProcessByMedicalTechnology(medicalTechnologyId)
    }
}
