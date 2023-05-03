/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller

import application.controller.manager.SurgeryBookingDigitalTwinManager
import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.patient.PatientData
import usecase.repository.BookingRepository

/**
 * The implementation of the [BookingRepository].
 */
class SurgeryBookingController(
    private val surgeryBookingDigitalTwinManager: SurgeryBookingDigitalTwinManager
) : BookingRepository {

    override fun getSurgeryBookingByPatient(patientId: PatientData.PatientId): SurgeryBooking? =
        this.surgeryBookingDigitalTwinManager.getSurgeryBookingByPatient(patientId)

    override fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean = this.surgeryBookingDigitalTwinManager.removePatientSurgeryBookingMapping(patientId, surgeryBookingId)

    override fun deleteSurgeryBooking(
        bookingId: SurgeryBookingData.SurgeryBookingId,
    ): Boolean =
        surgeryBookingDigitalTwinManager.deleteSurgeryBooking(bookingId)
}
