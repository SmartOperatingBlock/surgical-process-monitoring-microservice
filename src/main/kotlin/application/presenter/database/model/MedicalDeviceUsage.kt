/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.database.model

import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import kotlinx.serialization.Serializable

/**
 * Models the usage of a medical device.
 * @param implantableMedicalDeviceId the id of the implantable medical device.
 * @param processId the id of the process.
 */
@Serializable
data class MedicalDeviceUsage(
    val implantableMedicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
    val processId: ProcessData.ProcessId
)
