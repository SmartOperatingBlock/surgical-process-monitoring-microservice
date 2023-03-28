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
import application.service.SurgicalProcessServices
import entity.medicaldevice.MedicalDeviceData
import entity.process.ProcessData
import usecase.repository.MedicalDeviceRepository
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
    class ProcessInfoEventHandler(
        private val surgicalProcessRepository: SurgicalProcessRepository
    ) : EventHandler {

        override fun canHandle(event: Event<*>): Boolean = event.cast<ProcessEvent<*>> {
            this.data.cast<ProcessEventsPayloads.ProcessInfo>()
        }

        override fun consume(event: Event<*>) {
            event.cast<ProcessEvent<ProcessEventsPayloads.ProcessInfo>> {
                SurgicalProcessServices.UpdateSurgicalProcessStep(
                    ProcessData.ProcessId(this.data.processId),
                    ProcessData.ProcessStep.PATIENT_ON_OPERATING_TABLE,
                    surgicalProcessRepository
                ).execute()
            }
        }
    }

    private inline fun <reified T> Any?.cast(operation: T.() -> Boolean = { true }): Boolean = if (this is T) {
        operation()
    } else false
}