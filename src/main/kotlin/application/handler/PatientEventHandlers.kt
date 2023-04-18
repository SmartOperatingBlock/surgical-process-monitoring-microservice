/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.handler.ProcessEventHandlers.cast
import application.handler.ProcessEventHandlers.isSurgicalProcessOver
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.payloads.ProcessEventsPayloads
import application.service.PatientDataServices
import application.service.SurgicalProcessServices
import entity.patient.PatientData
import entity.process.ProcessData
import usecase.repository.PatientRepository
import usecase.repository.SurgicalProcessRepository
import java.time.Instant

/**
 * Module that contains all the handlers for patient events.
 */
object PatientEventHandlers {

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
                if (!isSurgicalProcessOver(this.data.processId, surgicalProcessRepository)) {
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
                } else {
                    false
                }
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
}
