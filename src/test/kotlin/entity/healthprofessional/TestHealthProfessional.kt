/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthprofessional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestHealthProfessional : StringSpec({

    "Health Professional Id should not be empty" {
        shouldThrow<IllegalArgumentException> {
            HealthProfessional(HealthProfessionalData.HealthProfessionalId(""))
        }
    }

    "Health professional should be equal to another health professional with same id" {
        val id = HealthProfessionalData.HealthProfessionalId("12345678")
        val first = HealthProfessional(id)
        val second = HealthProfessional(id)
        first shouldBe second
    }
})
