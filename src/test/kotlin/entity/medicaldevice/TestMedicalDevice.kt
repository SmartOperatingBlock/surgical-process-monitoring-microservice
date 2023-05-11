/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestMedicalDevice : StringSpec({

    "Medical technology should not have an empty id" {
        shouldThrow<IllegalArgumentException> {
            MedicalTechnology(
                MedicalDeviceData.MedicalTechnologyId(""),
                "x-ray-1",
                type = MedicalDeviceData.MedicalTechnologyType.X_RAY,
                inUse = false,
            )
        }
    }

    "Implantable medical device should not have empty id" {
        shouldThrow<IllegalArgumentException> {
            ImplantableMedicalDevice(
                MedicalDeviceData.ImplantableMedicalDeviceId(""),
                MedicalDeviceData.DeviceType.PACE_MAKER,
            )
        }
    }

    "Medical technology should be equal to another medical technology with same id" {
        val id = MedicalDeviceData.MedicalTechnologyId("mt-1")
        val first = MedicalTechnology(
            id,
            "x-ray-1",
            type = MedicalDeviceData.MedicalTechnologyType.X_RAY,
            inUse = false,
        )
        val second = MedicalTechnology(
            id,
            "x-ray-1",
            type = MedicalDeviceData.MedicalTechnologyType.X_RAY,
            inUse = false,
        )
        first shouldBe second
    }

    "Implantable medical device should be equal to another device with same id" {
        val id = MedicalDeviceData.ImplantableMedicalDeviceId("imd-1")
        val first = ImplantableMedicalDevice(
            id,
            MedicalDeviceData.DeviceType.PACE_MAKER,
        )
        val second = ImplantableMedicalDevice(id, MedicalDeviceData.DeviceType.PACE_MAKER)
        first shouldBe second
    }
})
