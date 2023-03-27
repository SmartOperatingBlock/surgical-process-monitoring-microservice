/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.patient

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestPatient : StringSpec({

    "Patient Id should not be empty" {
        shouldThrow<IllegalArgumentException> {
            Patient(PatientData.PatientId(""), medicalData = PatientData.MedicalData())
        }
    }

    "Patient should be equal to another patient with same id" {
        val id = PatientData.PatientId("patient-1")
        Patient(id, medicalData = PatientData.MedicalData()) shouldBe
            Patient(id, medicalData = PatientData.MedicalData())
    }
})
