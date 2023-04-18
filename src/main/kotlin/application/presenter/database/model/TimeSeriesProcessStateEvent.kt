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
 * Models a time series of a process state event.
 * @param dateTime the datetime of the process state event.
 * @param metadata metadata of the process state event.
 * @param value the value of the process state event.
 */
@Serializable
data class TimeSeriesProcessStateEvent(
    @Contextual
    val dateTime: Instant,
    val metadata: TimeSeriesProcessStateEventMetadata,
    val value: ProcessData.ProcessState,
)

/**
 * The metadata of a process state event.
 * @param processId the id of the process.
 */
@Serializable
data class TimeSeriesProcessStateEventMetadata(
    val processId: ProcessData.ProcessId,
)
