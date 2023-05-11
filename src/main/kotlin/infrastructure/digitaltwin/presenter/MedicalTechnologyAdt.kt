/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.presenter

import com.azure.digitaltwins.core.BasicDigitalTwin
import entity.medicaldevice.MedicalDeviceData
import entity.medicaldevice.MedicalTechnology
import infrastructure.digitaltwin.presenter.PropertyConversion.propertyAs

/**
 * The module for the Medical Device Azure Digital Twin.
 */
object MedicalTechnologyAdt {

    private const val NAME_PROPERTY = "name"
    private const val DESCRIPTION_PROPERTY = "description"
    private const val TYPE_PROPERTY = "type"

    private const val TYPE_ENDOSCOPE = "0"
    private const val TYPE_XRAY = "1"

    /** Extension method to convert a [BasicDigitalTwin] to a [MedicalTechnology]. */
    fun BasicDigitalTwin.toMedicalTechnology(value: Boolean): MedicalTechnology =
        MedicalTechnology(
            id = MedicalDeviceData.MedicalTechnologyId(this.id),
            name = this.contents[NAME_PROPERTY].propertyAs(""),
            description = this.contents[DESCRIPTION_PROPERTY].propertyAs(""),
            type = when (this.contents[TYPE_PROPERTY].propertyAs("not supported")) {
                TYPE_ENDOSCOPE -> MedicalDeviceData.MedicalTechnologyType.ENDOSCOPE
                TYPE_XRAY -> MedicalDeviceData.MedicalTechnologyType.X_RAY
                else -> throw IllegalArgumentException("medical device type not supported")
            },
            inUse = value,
        )
}
