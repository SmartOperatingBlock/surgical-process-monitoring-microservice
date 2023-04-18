/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.presenter

import com.azure.digitaltwins.core.BasicDigitalTwin
import entity.booking.SurgeryBooking
import entity.booking.SurgeryBookingData
import entity.healthprofessional.HealthProfessionalData
import entity.patient.PatientData
import infrastructure.digitaltwin.presenter.PropertyConversion.propertyAs
import java.time.Instant

/**
 * The module for the Surgery Booking Azure Digital Twin.
 */
object SurgeryBookingAdt {

    private const val DATETIME_PROPERTY = "booking_date_time"
    private const val TYPE_PROPERTY = "surgery_type"

    /** The relationship between patient and booking dt. */
    const val PATIENT_RELATIONSHIP = "rel_booking_associated_patient"
    private const val RESPONSIBLE_HEALTH_PROFESSIONAL_RELATIONSHIP = "rel_responsible_health_professional"

    /** Extension method to convert a [BasicDigitalTwin] to a [SurgeryBooking]. */
    fun BasicDigitalTwin.toSurgeryBooking(): SurgeryBooking =
        SurgeryBooking(
            id = SurgeryBookingData.SurgeryBookingId(this.id),
            dateTime = Instant.parse(this.contents[DATETIME_PROPERTY].propertyAs("")),
            healthProfessionalId = HealthProfessionalData.HealthProfessionalId(
                this.contents[RESPONSIBLE_HEALTH_PROFESSIONAL_RELATIONSHIP].propertyAs("")
            ),
            patientId = PatientData.PatientId(
                this.contents[PATIENT_RELATIONSHIP].propertyAs("")
            ),
            surgeryType = this.contents[TYPE_PROPERTY].propertyAs(""),
        )
}
