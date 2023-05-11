/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.patient.PatientData
import usecase.repository.BookingRepository

/** Module with all application services of surgery booking. */
object SurgeryBookingServices {

    /**
     * The [ApplicationService] to get a [SurgeryBooking] by a [patientId].
     */
    class GetSurgeryBookingByPatient(
        private val patientId: PatientData.PatientId,
        private val surgeryBookingRepository: BookingRepository,
    ) : ApplicationService<SurgeryBooking?> {
        override fun execute(): SurgeryBooking? = surgeryBookingRepository.getSurgeryBookingByPatient(patientId)
    }

    /**
     * The [ApplicationService] to delete a [SurgeryBooking] by a [bookingId].
     */
    class DeleteSurgeryBooking(
        private val bookingId: SurgeryBookingData.SurgeryBookingId,
        private val surgeryBookingRepository: BookingRepository,
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean = surgeryBookingRepository.deleteSurgeryBooking(bookingId)
    }
}
