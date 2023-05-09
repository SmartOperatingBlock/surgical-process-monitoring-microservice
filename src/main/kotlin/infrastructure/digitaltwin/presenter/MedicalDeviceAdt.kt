/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.presenter

import com.azure.digitaltwins.core.BasicDigitalTwin
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalDeviceData
import infrastructure.digitaltwin.presenter.PropertyConversion.propertyAs

/**
 * The module for the Medical Device Azure Digital Twin.
 */
object MedicalDeviceAdt {

    private const val TYPE_PROPERTY = "type"
    private const val TYPE_PACEMAKER = "0"
    private const val TYPE_CATHETER = "1"

    /** Extension method to convert a [BasicDigitalTwin] to a [ImplantableMedicalDevice]. */
    fun BasicDigitalTwin.toImplantableMedicalDevice(): ImplantableMedicalDevice =
        ImplantableMedicalDevice(
            id = MedicalDeviceData.ImplantableMedicalDeviceId(this.id),
            type = when (this.contents[TYPE_PROPERTY].propertyAs("not supported")) {
                TYPE_PACEMAKER -> MedicalDeviceData.DeviceType.PACE_MAKER
                TYPE_CATHETER -> MedicalDeviceData.DeviceType.CATHETER
                else -> throw IllegalArgumentException("medical device type not supported")
            }
        )
}
