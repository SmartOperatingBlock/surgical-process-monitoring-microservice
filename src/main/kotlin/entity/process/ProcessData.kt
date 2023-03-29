/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import kotlinx.serialization.Serializable

/** Module with all data necessary for the [SurgicalProcess]. */
object ProcessData {

    /** The [id] of the [SurgicalProcess]. */
    @Serializable
    data class ProcessId(val id: String) {
        init {
            require(this.id.isNotEmpty()) {
                "Process ID can not be empty!"
            }
        }
    }

    /** The different states of a [SurgicalProcess]. */
    enum class ProcessState {
        PRE_SURGERY, SURGERY, POST_SURGERY, INTERRUPTED
    }

    /** The different steps of a [SurgicalProcess]. */
    enum class ProcessStep {
        PATIENT_IN_PREPARATION, PATIENT_ON_OPERATING_TABLE, ANESTHESIA, SURGERY_IN_PROGRESS, END_OF_SURGERY,
    }
}
