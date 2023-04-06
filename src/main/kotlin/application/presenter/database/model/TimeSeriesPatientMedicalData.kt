/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.database.model

import entity.patient.PatientData
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Models a time series of a patient medical data.
 * @param dateTime the datetime of the medical data.
 * @param metadata metadata of the medical data.
 * @param value the value of the medical data.
 */
@Serializable
data class TimeSeriesPatientMedicalData(
    @Contextual
    val dateTime: Instant,
    val metadata: TimeSeriesPatientMedicalDataMetadata,
    val value: Double
)

/**
 * The metadata of a patient medical data.
 * @param patientId the id of the patient.
 * @param type the type of the medical data.
 */
@Serializable
data class TimeSeriesPatientMedicalDataMetadata(
    val patientId: PatientData.PatientId,
    val type: MedicalDataType
)

/**
 * Enum that represent all possible type of medical data.
 */
enum class MedicalDataType {
    DIASTOLIC_BLOOD_PRESSURE,
    SYSTOLIC_BLOOD_PRESSURE,
    HEARTH_BEAT,
    RESPIRATION_RATE,
    SATURATION_PERCENTAGE,
    BODY_TEMPERATURE
}

/**
 * Convert a map of [MedicalDataType] and [TimeSeriesPatientMedicalData] to a [PatientData.MedicalData].
 */
fun Map<MedicalDataType, TimeSeriesPatientMedicalData?>.toPatientMedicalData(
    startData: PatientData.MedicalData? = null
) = PatientData.MedicalData(
    heartBeat = this[MedicalDataType.HEARTH_BEAT]?.let {
        PatientData.HeartBeat(
            it.value.toInt()
        )
    } ?: startData?.heartBeat,
    diastolicBloodPressure = this[MedicalDataType.DIASTOLIC_BLOOD_PRESSURE]?.let {
        PatientData.DiastolicBloodPressure(
            it.value.toInt()
        )
    } ?: startData?.diastolicBloodPressure,
    systolicBloodPressure = this[MedicalDataType.SYSTOLIC_BLOOD_PRESSURE]?.let {
        PatientData.SystolicBloodPressure(
            it.value.toInt()
        )
    } ?: startData?.systolicBloodPressure,
    respiratoryRate = this[MedicalDataType.RESPIRATION_RATE]?.let {
        PatientData.RespiratoryRate(
            it.value.toInt()
        )
    } ?: startData?.respiratoryRate,
    saturationPercentage = this[MedicalDataType.SATURATION_PERCENTAGE]?.let {
        PatientData.SaturationPercentage(
            it.value.toInt()
        )
    } ?: startData?.saturationPercentage,
    bodyTemperature = this[MedicalDataType.BODY_TEMPERATURE]?.let {
        PatientData.BodyTemperature(
            it.value
        )
    } ?: startData?.bodyTemperature,
)

/**
 * Convert patient medical data to time series medical data.
 * @param dateTime the datetime of the medical data.
 * @param patientId the id of the patient.
 */
fun PatientData.MedicalData.toTimeSeriesMedicalData(dateTime: Instant, patientId: PatientData.PatientId) =
    getMedicalDataType(this).run {
        TimeSeriesPatientMedicalData(
            dateTime,
            TimeSeriesPatientMedicalDataMetadata(
                patientId,
                this.first
            ),
            this.second
        )
    }

private fun getMedicalDataType(medicalData: PatientData.MedicalData): Pair<MedicalDataType, Double> =
    listOfNotNull(
        medicalData.heartBeat?.let {
            Pair(MedicalDataType.HEARTH_BEAT, it.bpm.toDouble())
        },
        medicalData.diastolicBloodPressure?.let {
            Pair(MedicalDataType.DIASTOLIC_BLOOD_PRESSURE, it.pressure.toDouble())
        },
        medicalData.systolicBloodPressure?.let {
            Pair(MedicalDataType.SYSTOLIC_BLOOD_PRESSURE, it.pressure.toDouble())
        },
        medicalData.bodyTemperature?.let {
            Pair(MedicalDataType.BODY_TEMPERATURE, it.degree)
        },
        medicalData.respiratoryRate?.let {
            Pair(MedicalDataType.RESPIRATION_RATE, it.rate.toDouble())
        },
        medicalData.saturationPercentage?.let {
            Pair(MedicalDataType.SATURATION_PERCENTAGE, it.percentage.toDouble())
        }
    ).first()
