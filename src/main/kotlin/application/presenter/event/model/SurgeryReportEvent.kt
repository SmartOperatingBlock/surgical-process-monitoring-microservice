/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

/**
 * The event of Surgery Report.
 * It includes:
 * - the [key] of the event.
 * - the [dateTime] of the event.
 * - the [data] of the event.
 */
data class SurgeryReportEvent<E>(
    override val key: String = ProcessEventsKeys.SURGERY_REPORT_EVENT,
    override val dateTime: String,
    override val data: E
) : Event<E>
