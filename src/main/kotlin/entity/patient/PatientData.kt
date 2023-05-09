/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.patient

import kotlinx.serialization.Serializable

/** Module with all necessary data for [Patient]. */
object PatientData {

    /** The [id] of the [Patient]. */
    @Serializable
    data class PatientId(val id: String) {
        init {
            require(this.id.isNotEmpty()) {
                "Patient id code can not be empty!"
            }
        }
    }

    /** The [bpm] of the [Patient]. */
    @Serializable
    data class HeartBeat(val bpm: Int) {
        init {
            require(this.bpm >= 0) {
                "Beat per minute cannot be negative"
            }
        }
    }

    /** The Diastolic blood [pressure] of the [Patient]. */
    @Serializable
    data class DiastolicBloodPressure(val pressure: Int) {
        init {
            require(this.pressure >= 0) {
                "Diastolic blood pressure cannot be negative"
            }
        }
    }

    /** The Systolic blood [pressure] of the [Patient]. */
    @Serializable
    data class SystolicBloodPressure(val pressure: Int) {
        init {
            require(this.pressure >= 0) {
                "Systolic blood pressure cannot be negative"
            }
        }
    }

    /** The Respiratory [rate] of the [Patient]. */
    @Serializable
    data class RespiratoryRate(val rate: Int) {
        init {
            require(this.rate >= 0) {
                "Respiratory rate cannot be negative"
            }
        }
    }

    /** The Saturation [percentage] of the [Patient]. */
    @Serializable
    data class SaturationPercentage(val percentage: Int) {
        init {
            require(this.percentage >= 0) {
                "Saturation percentage cannot be negative"
            }
        }
    }

    /** The [degree] of body temperature of the [Patient] with the specific temperature [unit]. */
    @Serializable
    data class BodyTemperature(val degree: Double, val unit: TemperatureUnit = TemperatureUnit.CELSIUS) {
        init {
            require(this.degree >= 0) {
                "Body temperature cannot be negative"
            }
        }
    }

    /** The tax [code] of the [Patient]. */
    data class TaxCode(val code: String) {
        init {
            require(this.code.isNotEmpty()) {
                "Patient Tax Code cannot be empty"
            }
        }
    }

    /** The Temperature Unit. */
    enum class TemperatureUnit {
        CELSIUS
    }

    /** Wrap all the medical data of the [Patient] such as:
     * the [heartBeat],
     * the [diastolicBloodPressure],
     * the [systolicBloodPressure],
     * the [respiratoryRate],
     * the [saturationPercentage],
     * the [bodyTemperature]. */
    @Serializable
    data class MedicalData(
        val heartBeat: HeartBeat? = null,
        val diastolicBloodPressure: DiastolicBloodPressure? = null,
        val systolicBloodPressure: SystolicBloodPressure? = null,
        val respiratoryRate: RespiratoryRate? = null,
        val saturationPercentage: SaturationPercentage? = null,
        val bodyTemperature: BodyTemperature? = null
    )
}
