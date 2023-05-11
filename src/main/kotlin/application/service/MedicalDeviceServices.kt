/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalDeviceData
import entity.medicaldevice.MedicalTechnology
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
        private val medicalDeviceRepository: MedicalDeviceRepository,
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
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean = this.medicalDeviceRepository.addMedicalTechnologyUsage(
            medicalTechnologyId,
            processId,
            dateTime,
            inUse,
        )
    }

    /**
     * The [ApplicationService] to find a surgical process by a medical technology.
     */
    class FindProcessByMedicalTechnology(
        private val medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<ProcessData.ProcessId?> {

        override fun execute(): ProcessData.ProcessId? =
            this.medicalDeviceRepository.findSurgicalProcessByMedicalTechnology(medicalTechnologyId)
    }

    /**
     * The [ApplicationService] to get the usage of implantable medical devices in a process.
     */
    class GetMedicalDeviceUsageByProcessId(
        private val processId: ProcessData.ProcessId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<List<MedicalDeviceData.ImplantableMedicalDeviceId>> {

        override fun execute(): List<MedicalDeviceData.ImplantableMedicalDeviceId> =
            medicalDeviceRepository.getMedicalDeviceUsageByProcessId(processId)
    }

    /**
     * The [ApplicationService] to get the medical device by its id.
     */
    class GetMedicalDeviceById(
        private val implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<ImplantableMedicalDevice?> {
        override fun execute(): ImplantableMedicalDevice? =
            medicalDeviceRepository.getMedicalDeviceById(implantableMedicalDeviceId)
    }

    /**
     * The [ApplicationService] to get the medical technology usage in a process by its [processId].
     */
    class GetMedicalTechnologyUsageByProcessId(
        private val processId: ProcessData.ProcessId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<List<Triple<Instant, MedicalDeviceData.MedicalTechnologyId, Boolean>>> {
        override fun execute(): List<Triple<Instant, MedicalDeviceData.MedicalTechnologyId, Boolean>> =
            medicalDeviceRepository.getMedicalTechnologyUsageByProcessId(processId)
    }

    /**
     * The [ApplicationService] to get the medical technology by its id.
     */
    class GetMedicalTechnologyById(
        private val medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
        private val inUse: Boolean,
    ) : ApplicationService<MedicalTechnology?> {
        override fun execute(): MedicalTechnology? =
            medicalDeviceRepository.getMedicalTechnologyById(medicalTechnologyId, inUse)
    }

    /**
     * The [ApplicationService] to delete a medical device by its [implantableMedicalDeviceId].
     */
    class DeleteImplantableMedicalDevice(
        private val implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            medicalDeviceRepository.deleteMedicalDevice(implantableMedicalDeviceId)
    }
}
