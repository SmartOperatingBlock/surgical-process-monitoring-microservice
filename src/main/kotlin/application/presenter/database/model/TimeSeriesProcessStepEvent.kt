/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.database.model

import entity.process.ProcessData
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Models a time series of a process step event.
 * @param dateTime the datetime of the process step event.
 * @param metadata metadata of the process step event.
 * @param value the value of the process step event.
 */
@Serializable
data class TimeSeriesProcessStepEvent(
    @Contextual
    val dateTime: Instant,
    val metadata: TimeSeriesProcessStepEventMetadata,
    val value: ProcessData.ProcessStep,
)

/**
 * The metadata of a process step event.
 * @param processId the id of the process.
 */
@Serializable
data class TimeSeriesProcessStepEventMetadata(
    val processId: ProcessData.ProcessId,
)
