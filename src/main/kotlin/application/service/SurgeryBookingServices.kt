/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import entity.booking.SurgeryBooking
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
}
