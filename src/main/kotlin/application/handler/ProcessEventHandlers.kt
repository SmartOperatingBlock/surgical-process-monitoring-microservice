/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.controller.manager.EventProducer
import application.handler.ProcessEventHandlers.cast
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.SurgeryReportEvent
import application.presenter.event.model.payloads.ProcessEventsPayloads
import application.presenter.event.serialization.toSurgeryReportDto
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
                    medicalDeviceRepository,
                ).execute()
            }
        }
    }

    /** The handler for medical technology usage event. */
    class MedicalTechnologyUsageEventHandler(
        private val medicalDeviceRepository: MedicalDeviceRepository,
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.MedicalTechnologyUsage>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.MedicalTechnologyUsage>> {
                val processId = MedicalDeviceServices.FindProcessByMedicalTechnology(
                    MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                    medicalDeviceRepository,
                ).execute()
                if (processId != null) {
                    MedicalDeviceServices.AddMedicalTechnologyUsage(
                        MedicalDeviceData.MedicalTechnologyId(this.data.medicalTechnologyID),
                        processId,
                        Instant.parse(this.dateTime),
                        this.data.inUse,
                        medicalDeviceRepository,
                    ).execute()
                } else {
                    false
                }
            }
        }
    }

    /**
     * The handler for Patient tracked event.
     */
    class PatientTrackedEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
        private val surgeryBookingRepository: BookingRepository,
        private val patientRepository: PatientRepository,
        private val medicalDeviceRepository: MedicalDeviceRepository,
        private val eventProducer: EventProducer,
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
                            managePreOperatingRoomPatientEntrance(
                                surgicalProcess,
                                this,
                                surgeryBookingRepository,
                                surgicalProcessRepository,
                            )
                            true
                        } else {
                            managePreOperatingRoomPatientExit(
                                surgicalProcess,
                                surgicalProcessRepository,
                                patientRepository,
                                surgeryBookingRepository,
                                medicalDeviceRepository,
                                this,
                                eventProducer,
                            )
                            true
                        }
                    }
                    ProcessEventsPayloads.RoomType.OPERATING_ROOM -> {
                        if (this.data.entered) {
                            manageOperatingRoomPatientEntrance(surgicalProcess, surgicalProcessRepository, this)
                        }
                        true
                    }
                }
            }
        }
    }

    private fun managePreOperatingRoomPatientEntrance(
        surgicalProcess: SurgicalProcess?,
        event: ProcessEvent<ProcessEventsPayloads.PatientTracked>,
        surgeryBookingRepository: BookingRepository,
        surgicalProcessRepository: SurgicalProcessRepository,
    ) {
        if (surgicalProcess == null) {
            val surgeryBooking =
                SurgeryBookingServices.GetSurgeryBookingByPatient(
                    PatientData.PatientId(event.data.patientId),
                    surgeryBookingRepository,
                ).execute()
            if (surgeryBooking != null) {
                SurgicalProcessServices.CreateSurgicalProcess(
                    SurgicalProcess(
                        ProcessData.ProcessId(
                            "${event.data.patientId}-${event.dateTime.replace(":",".")}",
                        ),
                        Instant.parse(event.dateTime),
                        surgeryBooking.surgeryType,
                        surgeryBooking.patientId,
                        surgeryBooking.healthProfessionalId,
                        preOperatingRoom = Room(
                            RoomData.RoomId(event.data.roomId),
                            type = RoomData.RoomType.PRE_POST_OPERATING_ROOM,
                        ),
                        state = ProcessData.ProcessState.PRE_SURGERY,
                        step = ProcessData.ProcessStep.PATIENT_IN_PREPARATION,
                    ),
                    surgicalProcessRepository,
                ).execute()?.let {
                    SurgicalProcessServices.UpdateSurgicalProcessState(
                        it.id,
                        it.dateTime,
                        ProcessData.ProcessState.PRE_SURGERY,
                        surgicalProcessRepository,
                    ).execute()
                    SurgicalProcessServices.UpdateSurgicalProcessStep(
                        it.id,
                        it.dateTime,
                        ProcessData.ProcessStep.PATIENT_IN_PREPARATION,
                        surgicalProcessRepository,
                    ).execute()
                }
            }
        } else {
            SurgicalProcessServices.UpdateSurgicalProcessState(
                surgicalProcess.id,
                Instant.parse(event.dateTime),
                ProcessData.ProcessState.POST_SURGERY,
                surgicalProcessRepository,
            ).execute()
            SurgicalProcessServices.UpdateSurgicalProcessStep(
                surgicalProcess.id,
                Instant.parse(event.dateTime),
                ProcessData.ProcessStep.PATIENT_UNDER_OBSERVATION,
                surgicalProcessRepository,
            ).execute()
            SurgicalProcessServices.UpdateSurgicalProcessRoom(
                surgicalProcess.id,
                surgicalProcess.operatingRoom?.id?.id,
                Room(RoomData.RoomId(event.data.roomId), type = RoomData.RoomType.OPERATING_ROOM),
                surgicalProcessRepository,
            ).execute()
        }
    }

    private fun manageOperatingRoomPatientEntrance(
        surgicalProcess: SurgicalProcess?,
        surgicalProcessRepository: SurgicalProcessRepository,
        event: ProcessEvent<ProcessEventsPayloads.PatientTracked>,
    ) {
        surgicalProcess?.let {
            SurgicalProcessServices.UpdateSurgicalProcessRoom(
                it.id,
                it.preOperatingRoom?.id?.id,
                Room(RoomData.RoomId(event.data.roomId), type = RoomData.RoomType.OPERATING_ROOM),
                surgicalProcessRepository,
            ).execute()
        }
    }

    private fun managePreOperatingRoomPatientExit(
        surgicalProcess: SurgicalProcess?,
        surgicalProcessRepository: SurgicalProcessRepository,
        patientRepository: PatientRepository,
        surgeryBookingRepository: BookingRepository,
        medicalDeviceRepository: MedicalDeviceRepository,
        event: ProcessEvent<ProcessEventsPayloads.PatientTracked>,
        eventProducer: EventProducer,
    ) {
        if (surgicalProcess != null &&
            surgicalProcess.state == ProcessData.ProcessState.POST_SURGERY &&
            !isSurgicalProcessOver(
                surgicalProcess.id.id,
                surgicalProcessRepository,
            )
        ) {
            SurgicalProcessServices.UpdateSurgicalProcessState(
                surgicalProcess.id,
                Instant.parse(event.dateTime),
                ProcessData.ProcessState.TERMINATED,
                surgicalProcessRepository,
            ).execute()
            SurgicalProcessServices.GetSurgicalProcessStates(
                surgicalProcess.id,
                surgicalProcessRepository,
            ).execute().also { processStates ->
                eventProducer.produceEvent(
                    SurgeryReportEvent(
                        dateTime = Instant.now().toString(),
                        data = SurgeryReport(
                            surgicalProcess.id,
                            surgicalProcess.type,
                            surgicalProcess.patientId,
                            PatientDataServices.GetPatientTaxCode(
                                surgicalProcess.patientId,
                                patientRepository,
                            ).execute(),
                            surgicalProcess.healthProfessionalId,
                            surgicalProcess.preOperatingRoom,
                            surgicalProcess.operatingRoom,
                            processStates,
                            SurgicalProcessServices.GetSurgicalProcessSteps(
                                surgicalProcess.id,
                                surgicalProcessRepository,
                            ).execute(),
                            PatientDataServices.GetPatientMedicalData(
                                surgicalProcess.patientId,
                                getStartAndEndProcessTime(processStates).first,
                                getStartAndEndProcessTime(processStates).second,
                                patientRepository,
                            ).execute(),
                            MedicalDeviceServices.GetMedicalDeviceUsageByProcessId(
                                surgicalProcess.id,
                                medicalDeviceRepository,
                            ).execute().mapNotNull {
                                MedicalDeviceServices.GetMedicalDeviceById(it, medicalDeviceRepository).execute()
                            },
                            MedicalDeviceServices.GetMedicalTechnologyUsageByProcessId(
                                surgicalProcess.id,
                                medicalDeviceRepository,
                            ).execute().map {
                                Pair(
                                    it.first,
                                    MedicalDeviceServices.GetMedicalTechnologyById(
                                        it.second,
                                        medicalDeviceRepository,
                                        it.third,
                                    ).execute(),
                                )
                            }.filterPairs(),
                        ).toSurgeryReportDto(),
                    ),
                )
            }
            SurgicalProcessServices.DeleteSurgicalProcess(surgicalProcess.id, surgicalProcessRepository).execute()
            SurgeryBookingServices.GetSurgeryBookingByPatient(
                PatientData.PatientId(event.data.patientId),
                surgeryBookingRepository,
            ).execute()?.let {
                SurgeryBookingServices.DeleteSurgeryBooking(
                    it.id,
                    surgeryBookingRepository,
                ).execute()
            }
            PatientDataServices.DeletePatient(surgicalProcess.patientId, patientRepository).execute()
            MedicalDeviceServices.GetMedicalDeviceUsageByProcessId(
                surgicalProcess.id,
                medicalDeviceRepository,
            ).execute().forEach {
                MedicalDeviceServices.DeleteImplantableMedicalDevice(it, medicalDeviceRepository).execute()
            }
        }
    }

    /**
     * The handler for Emergency Surgery events.
     */
    class EmergencySurgeryEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
        private val patientRepository: PatientRepository,
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.EmergencySurgery>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.EmergencySurgery>> {
                val patient = PatientDataServices.CreatePatient(
                    PatientData.PatientId(
                        "emergency-patient-${Instant.now()}",
                    ),
                    patientRepository,
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
                                type = RoomData.RoomType.OPERATING_ROOM,
                            ),
                            ProcessData.ProcessState.SURGERY,
                        ),
                        surgicalProcessRepository,
                    ).execute() != null
                } else {
                    false
                }
            }
        }
    }

    /**
     * The handler for Process manual events.
     */
    class ProcessManualEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository,
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.ProcessManualEvent>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.ProcessManualEvent>> {
                val surgicalProcess: SurgicalProcess? =
                    SurgicalProcessServices
                        .GetCurrentSurgicalProcesses(surgicalProcessRepository).execute().firstOrNull {
                            it.operatingRoom?.id?.id == this.data.roomId
                        }
                surgicalProcess?.let {
                    when (this.data.manualEvent) {
                        ProcessData.ProcessStep.ANESTHESIA.toString(),
                        ProcessData.ProcessStep.END_OF_SURGERY.toString(),
                        -> {
                            SurgicalProcessServices.UpdateSurgicalProcessStep(
                                it.id,
                                Instant.parse(this.dateTime),
                                ProcessData.ProcessStep.valueOf(this.data.manualEvent),
                                surgicalProcessRepository,
                            ).execute()
                        }
                        ProcessData.ProcessState.INTERRUPTED.toString() -> {
                            SurgicalProcessServices.UpdateSurgicalProcessState(
                                it.id,
                                Instant.parse(this.dateTime),
                                ProcessData.ProcessState.valueOf(this.data.manualEvent),
                                surgicalProcessRepository,
                            ).execute()
                        }
                        else -> {}
                    }
                }
                true
            }
        }
    }

    private fun getStartAndEndProcessTime(
        processStates: List<Pair<Instant, ProcessData.ProcessState>>,
    ): Pair<Instant, Instant> =
        Pair(
            processStates.first {
                it.second == ProcessData.ProcessState.PRE_SURGERY
            }.first,
            processStates.first {
                it.second == ProcessData.ProcessState.TERMINATED || it.second == ProcessData.ProcessState.INTERRUPTED
            }.first,
        )

    /** Utility function to cast en event to its type. */
    inline fun <reified T> Any?.cast(operation: T.() -> Boolean = { true }): Boolean = if (this is T) {
        operation()
    } else {
        false
    }

    /**
     * Utility function to filter the null values of a list of pair.
     */
    fun <T, U> List<Pair<T?, U?>>.filterPairs(): List<Pair<T, U>> =
        mapNotNull { (t, u) ->
            if (t == null || u == null) null else t to u
        }

    /**
     * Utility function to check if a [SurgicalProcess] is over.
     */
    fun isSurgicalProcessOver(processId: String, surgicalProcessRepository: SurgicalProcessRepository): Boolean {
        val process = SurgicalProcessServices.GetSurgicalProcessById(
            ProcessData.ProcessId(processId),
            surgicalProcessRepository,
        ).execute()
        return if (process != null) {
            process.state == ProcessData.ProcessState.INTERRUPTED ||
                process.state == ProcessData.ProcessState.TERMINATED
        } else {
            false
        }
    }
}
