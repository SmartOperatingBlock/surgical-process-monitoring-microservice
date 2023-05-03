/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.patient.PatientData

/** This interface models the operation on Digital Twins of [SurgeryBooking]. */
interface SurgeryBookingDigitalTwinManager {

    /** Get a [SurgeryBooking] by the [patientId]. */
    fun getSurgeryBookingByPatient(patientId: PatientData.PatientId): SurgeryBooking?

    /** Remove the relationship between a patient with a [patientId] and a booking with the [surgeryBookingId]. */
    fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean

    /** Delete a [SurgeryBooking] by the [bookingId]. */
    fun deleteSurgeryBooking(bookingId: SurgeryBookingData.SurgeryBookingId): Boolean
}
