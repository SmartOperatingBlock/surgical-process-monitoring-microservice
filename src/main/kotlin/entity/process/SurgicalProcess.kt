/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import entity.healthprofessional.HealthProfessional
import entity.patient.Patient
import entity.room.Room

/** The model of a Surgical Process composed by:
 * - the [id] of the process
 * - the [patient] that is going to be operated
 * - the [healthProfessional] in charge of the process
 * - the [room] in which the process is done
 * - the current [state] of the process
 * - the current [step] of the process.
 */
data class SurgicalProcess(
    val id: ProcessData.ProcessId,
    val patient: Patient,
    val healthProfessional: HealthProfessional,
    val room: Room,
    val state: ProcessData.ProcessState,
    val step: ProcessData.ProcessStep?
)
