/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.payloads.ProcessEventsPayloads
import application.service.MedicalDeviceServices
import application.service.PatientDataServices
import application.service.SurgeryBookingServices
import application.service.SurgicalProcessServices
import entity.medicaldevice.MedicalDeviceData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.room.Room
import entity.room.RoomData
import usecase.repository.BookingRepository
import usecase.repository.MedicalDeviceRepository
import usecase.repository.PatientRepository
import usecase.repository.SurgicalProcessRepository
import java.time.Instant

/**
 * Module that contains all the handlers for process events.
 */
object EventHandlers {

    /** The handler for medical device usage event. */
    class MedicalDeviceUsageEventHandler(private val medicalDeviceRepository: MedicalDeviceRepository) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.MedicalDeviceUsage>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.MedicalDeviceUsage>> {
                MedicalDeviceServices.AddMedicalDeviceUsage(
                    MedicalDeviceData.ImplantableMedicalDeviceId(this.data.medicalDeviceID),
                    ProcessData.ProcessId(this.data.processId),
                    medicalDeviceRepository
                ).execute()
            }
        }
    }

    /** The handler for medical technology usage event. */
    class MedicalTechnologyUsageEventHandler(
        private val medicalDeviceRepository: MedicalDeviceRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.MedicalTechnologyUsage>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.MedicalTechnologyUsage>> {
                val process = MedicalDeviceServices.FindProcessByMedicalTechnology(
                    MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                    medicalDeviceRepository
                ).execute()
                if (process != null) {
                    MedicalDeviceServices.AddMedicalTechnologyUsage(
                        MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                        process.id,
                        Instant.parse(this.dateTime),
                        this.data.inUse,
                        medicalDeviceRepository
                    ).execute()
                } else false
            }
        }
    }

    /**
     * The handler for process information events.
     */
    class PatientOnOperatingTableEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientOnOperatingTable>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientOnOperatingTable>> {
                SurgicalProcessServices.UpdateSurgicalProcessState(
                    ProcessData.ProcessId(this.data.processId),
                    Instant.parse(this.dateTime),
                    ProcessData.ProcessState.SURGERY,
                    surgicalProcessRepository
                ).execute() &&
                    SurgicalProcessServices.UpdateSurgicalProcessStep(
                        ProcessData.ProcessId(this.data.processId),
                        Instant.parse(this.dateTime),
                        ProcessData.ProcessStep.PATIENT_ON_OPERATING_TABLE,
                        surgicalProcessRepository
                    ).execute()
            }
        }
    }

    /**
     * The handler for Patient Body Temperature update events.
     */
    class BodyTemperatureUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.BodyTemperature>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.BodyTemperature>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(bodyTemperature = PatientData.BodyTemperature(this.data.data.temperature)),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient Diastolic Pressure update events.
     */
    class DiastolicPressureUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.DiastolicPressure>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.DiastolicPressure>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(
                        diastolicBloodPressure = PatientData.DiastolicBloodPressure(this.data.data.pressure)
                    ),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient Systolic Pressure update events.
     */
    class SystolicPressureUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.SystolicPressure>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.SystolicPressure>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(
                        systolicBloodPressure = PatientData.SystolicBloodPressure(this.data.data.pressure)
                    ),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient Diastolic Pressure update events.
     */
    class RespiratoryRateUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.RespiratoryRate>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.RespiratoryRate>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(
                        respiratoryRate = PatientData.RespiratoryRate(this.data.data.rate)
                    ),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient saturation update events.
     */
    class SaturationUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.Saturation>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.Saturation>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(
                        saturationPercentage = PatientData.SaturationPercentage(this.data.data.saturation)
                    ),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient heart beat update events.
     */
    class HeartbeatUpdateEventHandler(
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.Heartbeat>>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientData<ProcessEventsPayloads.Heartbeat>>> {
                PatientDataServices.UpdatePatientMedicalData(
                    PatientData.PatientId(this.data.patientId),
                    PatientData.MedicalData(
                        heartBeat = PatientData.HeartBeat(this.data.data.heartbeat)
                    ),
                    Instant.parse(this.dateTime),
                    patientRepository
                ).execute()
            }
        }
    }

    /**
     * The handler for Patient Diastolic Pressure update events.
     */
    class PatientTrackedEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
        private val surgeryBookingRepository: BookingRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientTracked>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientTracked>> {
                when (this.data.roomType) {
                    ProcessEventsPayloads.RoomType.PRE_OPERATING_ROOM -> {
                        val surgicalProcess: SurgicalProcess? =
                            SurgicalProcessServices.GetCurrentSurgicalProcesses(surgicalProcessRepository).execute()
                                .firstOrNull {
                                    it.patient?.id?.id == this.data.patientId
                                }
                        if (this.data.entered) {
                            if (surgicalProcess == null) {
                                val surgeryBooking =
                                    SurgeryBookingServices.GetSurgeryBookingByPatient(
                                        PatientData.PatientId(this.data.patientId),
                                        surgeryBookingRepository
                                    ).execute()
                                if (surgeryBooking != null) {
                                    SurgicalProcessServices.CreateSurgicalProcess(
                                        SurgicalProcess(
                                            ProcessData.ProcessId(
                                                "${surgeryBooking.surgeryType}-${this.data.patientId}${this.dateTime}"
                                            ),
                                            Instant.now(),
                                            surgeryBooking.surgeryType,
                                            surgeryBooking.patient,
                                            surgeryBooking.healthProfessional,
                                            Room(
                                                RoomData.RoomId(this.data.roomId),
                                                type = RoomData.RoomType.PRE_POST_OPERATING_ROOM
                                            ),
                                            ProcessData.ProcessState.PRE_SURGERY,
                                            ProcessData.ProcessStep.PATIENT_IN_PREPARATION
                                        ),
                                        surgicalProcessRepository
                                    ).execute() != null
                                } else false
                            } else {
                                SurgicalProcessServices.UpdateSurgicalProcessState(
                                    surgicalProcess.id,
                                    Instant.parse(this.dateTime),
                                    ProcessData.ProcessState.POST_SURGERY,
                                    surgicalProcessRepository
                                ).execute()
                            }
                        } else {
                            if (surgicalProcess != null) {
                                SurgicalProcessServices.UpdateSurgicalProcessState(
                                    surgicalProcess.id,
                                    Instant.parse(event.dateTime),
                                    ProcessData.ProcessState.TERMINATED,
                                    surgicalProcessRepository
                                ).execute()
                            } else false
                        }
                    }
                    ProcessEventsPayloads.RoomType.OPERATING_ROOM -> {
                        false
                    }
                }
            }
        }
    }

    /**
     * The handler for Emergency Surgery events.
     */
    class EmergencySurgeryEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
        private val patientRepository: PatientRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.EmergencySurgery>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.EmergencySurgery>> {
                SurgicalProcessServices.CreateSurgicalProcess(
                    SurgicalProcess(
                        ProcessData.ProcessId("emergency-${this.data.roomId}-${Instant.now()}"),
                        Instant.now(),
                        "Emergency",
                        PatientDataServices.CreatePatient(
                            PatientData.PatientId(
                                "emergency-patient-${Instant.now()}"
                            ),
                            patientRepository
                        ).execute(),
                        null,
                        Room(RoomData.RoomId(this.data.roomId), type = RoomData.RoomType.OPERATING_ROOM),
                        ProcessData.ProcessState.SURGERY
                    ),
                    surgicalProcessRepository
                ).execute() != null
            }
        }
    }

    private inline fun <reified T> Any?.cast(operation: T.() -> Boolean = { true }): Boolean = if (this is T) {
        operation()
    } else false
}
