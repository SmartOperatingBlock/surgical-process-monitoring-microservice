/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin

import application.controller.manager.MedicalDeviceDigitalTwinManager
import application.controller.manager.PatientDigitalTwinManager
import application.controller.manager.ProcessDigitalTwinManager
import application.controller.manager.SurgeryBookingDigitalTwinManager
import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.medicaldevice.MedicalDeviceData
import entity.patient.Patient
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room

class MockDigitalTwinManager :
    MedicalDeviceDigitalTwinManager,
    PatientDigitalTwinManager,
    ProcessDigitalTwinManager,
    SurgeryBookingDigitalTwinManager {

    private val surgicalProcesses: MutableSet<SurgicalProcess> = mutableSetOf()
    private val surgeryBookings: MutableSet<SurgeryBooking> = mutableSetOf()

    override fun findSurgicalProcessByMedicalTechnology(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId
    ): ProcessData.ProcessId? = null

    override fun createPatientDT(patientId: PatientData.PatientId): Patient? = null

    override fun deletePatientDT(patientId: PatientData.PatientId): Boolean = true

    override fun createSurgicalProcess(process: SurgicalProcess): Boolean = surgicalProcesses.add(process)

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean = surgicalProcesses.any { it.id == processId }

    override fun updateSurgicalProcessStep(processId: ProcessData.ProcessId, step: ProcessData.ProcessStep): Boolean =
        surgicalProcesses.any { it.id == processId }

    override fun updateSurgicalProcessRoom(
        processId: ProcessData.ProcessId,
        latestRoomId: String?,
        room: Room
    ): Boolean = true

    override fun deleteSurgicalProcess(processId: ProcessData.ProcessId): Boolean = true

    override fun getSurgeryBookingByPatient(patientId: PatientData.PatientId): SurgeryBooking? =
        surgeryBookings.find { it.patientId == patientId }

    override fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean = true

    override fun deleteSurgeryBooking(bookingId: SurgeryBookingData.SurgeryBookingId): Boolean = true
}
