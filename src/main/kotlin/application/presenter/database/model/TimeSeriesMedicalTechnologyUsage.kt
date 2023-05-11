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
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Models a time series of a usage of a medical technology.
 * @param dateTime the datetime of the usage.
 * @param metadata metadata of the medical technology usage.
 * @param value the value of the medical technology usage.
 */
@Serializable
data class TimeSeriesMedicalTechnologyUsage(
    @Contextual
    val dateTime: Instant,
    val metadata: TimeSeriesMedicalTechnologyUsageMetadata,
    val value: Boolean,
)

/**
 * The metadata of a medical technology time series.
 * @param medicalTechnologyId the id of the medical technology.
 * @param processId the id of the process.
 */
@Serializable
data class TimeSeriesMedicalTechnologyUsageMetadata(
    val medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
    val processId: ProcessData.ProcessId,
)
