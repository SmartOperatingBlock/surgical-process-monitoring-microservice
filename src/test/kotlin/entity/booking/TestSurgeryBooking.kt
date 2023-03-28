/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.booking

import entity.healthprofessional.HealthProfessional
import entity.healthprofessional.HealthProfessionalData
import entity.patient.Patient
import entity.patient.PatientData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TestSurgeryBooking : StringSpec({

    val healthProfessional = HealthProfessional(HealthProfessionalData.HealthProfessionalId("12345678"))
    val patient = Patient(PatientData.PatientId("01"), medicalData = PatientData.MedicalData())

    "Surgery booking id should not be empty" {
        shouldThrow<IllegalArgumentException> {
            SurgeryBooking(
                SurgeryBookingData.SurgeryBookingId(""),
                Instant.now(),
                healthProfessional,
                patient,
                "Colonscopy"
            )
        }
    }

    "Surgery booking should be equal to other booking with same id" {
        val id = SurgeryBookingData.SurgeryBookingId("sb-1")
        SurgeryBooking(
            id,
            Instant.now(),
            healthProfessional,
            patient,
            "Colonscopy"
        ) shouldBe SurgeryBooking(
            id,
            Instant.now(),
            healthProfessional,
            patient,
            "Colonscopy"
        )
    }
})