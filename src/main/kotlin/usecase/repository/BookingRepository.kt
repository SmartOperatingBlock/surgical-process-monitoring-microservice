/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.patient.PatientData

/**
 * Interface that models the repository to manage surgery bookings.
 */
interface BookingRepository {

    /**
     * Get a [SurgeryBooking] given the patient id.
     * @return null if the surgery booking doesn't exist, the surgery booking otherwise.
     */
    fun getSurgeryBookingByPatient(
        patientId: PatientData.PatientId
    ): SurgeryBooking?

    /**
     * Remove the mapping between a surgery booking and a patient.
     * @return true if correctly removed, false otherwise.
     */
    fun removePatientSurgeryBookingMapping(
        patientId: PatientData.PatientId,
        surgeryBookingId: SurgeryBookingData.SurgeryBookingId
    ): Boolean
}
