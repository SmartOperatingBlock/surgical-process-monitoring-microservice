/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.controller.manager.EventProducer
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.SurgeryReportEvent
import application.presenter.event.model.payloads.ProcessEventsPayloads
import application.service.MedicalDeviceServices
import application.service.PatientDataServices
import application.service.SurgeryBookingServices
import application.service.SurgicalProcessServices
import entity.medicaldevice.MedicalDeviceData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import entity.report.SurgeryReport
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
object ProcessEventHandlers {

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
                val processId = MedicalDeviceServices.FindProcessByMedicalTechnology(
                    MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                    medicalDeviceRepository
                ).execute()
                if (processId != null) {
                    MedicalDeviceServices.AddMedicalTechnologyUsage(
                        MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                        processId,
                        Instant.parse(this.dateTime),
                        this.data.inUse,
                        medicalDeviceRepository
                    ).execute()
                } else false
            }
        }
    }

    /**
     * The handler for Patient Diastolic Pressure update events.
     */
    class PatientTrackedEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
        private val surgeryBookingRepository: BookingRepository,
        private val eventProducer: EventProducer
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.PatientTracked>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.PatientTracked>> {
                val surgicalProcess: SurgicalProcess? =
                    SurgicalProcessServices.GetCurrentSurgicalProcesses(surgicalProcessRepository).execute()
                        .firstOrNull {
                            it.patientId.id == this.data.patientId
                        }
                when (this.data.roomType) {
                    ProcessEventsPayloads.RoomType.PRE_OPERATING_ROOM -> {
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
                                                "${this.data.patientId}-${this.dateTime.replace(":",".")}"
                                            ),
                                            Instant.parse(this.dateTime),
                                            surgeryBooking.surgeryType,
                                            surgeryBooking.patientId,
                                            surgeryBooking.healthProfessionalId,
                                            preOperatingRoom = Room(
                                                RoomData.RoomId(this.data.roomId),
                                                type = RoomData.RoomType.PRE_POST_OPERATING_ROOM
                                            ),
                                            state = ProcessData.ProcessState.PRE_SURGERY,
                                            step = ProcessData.ProcessStep.PATIENT_IN_PREPARATION
                                        ),
                                        surgicalProcessRepository
                                    ).execute()?.let {
                                        SurgicalProcessServices.UpdateSurgicalProcessState(
                                            it.id,
                                            it.dateTime,
                                            ProcessData.ProcessState.PRE_SURGERY,
                                            surgicalProcessRepository
                                        ).execute()
                                        SurgicalProcessServices.UpdateSurgicalProcessStep(
                                            it.id,
                                            it.dateTime,
                                            ProcessData.ProcessStep.PATIENT_IN_PREPARATION,
                                            surgicalProcessRepository
                                        ).execute()
                                    }
                                    true
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
                            if (surgicalProcess != null && !isSurgicalProcessOver(
                                    surgicalProcess.id.id,
                                    surgicalProcessRepository
                                )
                            ) {
                                SurgicalProcessServices.UpdateSurgicalProcessState(
                                    surgicalProcess.id,
                                    Instant.parse(event.dateTime),
                                    ProcessData.ProcessState.TERMINATED,
                                    surgicalProcessRepository
                                ).execute()
                                val processStates = SurgicalProcessServices.GetSurgicalProcessStates(
                                    surgicalProcess.id,
                                    surgicalProcessRepository
                                ).execute()
                                val processSteps = SurgicalProcessServices.GetSurgicalProcessSteps(
                                    surgicalProcess.id,
                                    surgicalProcessRepository
                                ).execute()
                                surgicalProcess.healthProfessionalId?.let { hpId ->
                                    surgicalProcess.preOperatingRoom?.let { preOpId ->
                                        surgicalProcess.operatingRoom?.let { opId ->
                                            SurgeryReport(
                                                surgicalProcess.id,
                                                surgicalProcess.type,
                                                surgicalProcess.patientId,
                                                hpId,
                                                preOpId,
                                                opId,
                                                processStates,
                                                processSteps
                                            )
                                        }
                                    }
                                }?.let {
                                    eventProducer.produceEvent(
                                        SurgeryReportEvent(
                                            dateTime = Instant.now().toString(),
                                            data = it
                                        )
                                    )
                                }
                                true
                            } else false
                        }
                    }
                    ProcessEventsPayloads.RoomType.OPERATING_ROOM -> {
                        surgicalProcess?.let {
                            SurgicalProcessServices.UpdateSurgicalProcessRoom(
                                it.id,
                                Room(RoomData.RoomId(this.data.roomId), type = RoomData.RoomType.OPERATING_ROOM),
                                surgicalProcessRepository
                            ).execute()
                        }
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
                val patient = PatientDataServices.CreatePatient(
                    PatientData.PatientId(
                        "emergency-patient-${Instant.now()}"
                    ),
                    patientRepository
                ).execute()
                if (patient != null) {
                    SurgicalProcessServices.CreateSurgicalProcess(
                        SurgicalProcess(
                            ProcessData.ProcessId("emergency-${this.data.roomId}-${Instant.now()}"),
                            Instant.parse(event.dateTime),
                            "Emergency",
                            patient.id,
                            null,
                            preOperatingRoom = null,
                            operatingRoom = Room(
                                RoomData.RoomId(this.data.roomId),
                                type = RoomData.RoomType.OPERATING_ROOM
                            ),
                            ProcessData.ProcessState.SURGERY
                        ),
                        surgicalProcessRepository
                    ).execute() != null
                } else {
                    false
                }
            }
        }
    }

    /** Utility function to cast en event to its type. */
    inline fun <reified T> Any?.cast(operation: T.() -> Boolean = { true }): Boolean = if (this is T) {
        operation()
    } else false

    /**
     * Utility function to check if a [SurgicalProcess] is over.
     */
    fun isSurgicalProcessOver(processId: String, surgicalProcessRepository: SurgicalProcessRepository): Boolean {
        val process = SurgicalProcessServices.GetSurgicalProcessById(
            ProcessData.ProcessId(processId),
            surgicalProcessRepository
        ).execute()
        return if (process != null) {
            process.state != ProcessData.ProcessState.INTERRUPTED &&
                process.state != ProcessData.ProcessState.TERMINATED
        } else {
            false
        }
    }
}
