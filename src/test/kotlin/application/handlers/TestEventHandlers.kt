/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handlers

import application.controller.MedicalDeviceController
import application.controller.PatientDataController
import application.controller.SurgeryBookingController
import application.controller.SurgicalProcessController
import application.handler.ProcessEventHandlers
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEvent
import application.presenter.event.model.ProcessEventsKeys
import application.presenter.event.model.payloads.ProcessEventsPayloads
import infrastructure.database.DatabaseManager
import infrastructure.digitaltwin.MockDigitalTwinManager
import infrastructure.event.MockEventProducer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TestEventHandlers : StringSpec({

    val databaseManager by lazy { DatabaseManager("mongodb://localhost:27017") }
    val mockDigitalTwinManager = MockDigitalTwinManager()

    fun surgicalProcessController() = SurgicalProcessController(databaseManager, mockDigitalTwinManager)
    fun surgeryBookingController() = SurgeryBookingController(mockDigitalTwinManager)
    fun medicalDeviceController() = MedicalDeviceController(databaseManager, mockDigitalTwinManager)
    fun patientController() = PatientDataController(databaseManager, mockDigitalTwinManager)

    "Patient Tracked event should be handled correctly" {
        val event: Event<*> = ProcessEvent<ProcessEventsPayloads.PatientTracked>(
            key = ProcessEventsKeys.PATIENT_TRACKED_EVENT,
            data = ProcessEventsPayloads.PatientTracked(
                "patient-id",
                "room-id",
                true,
                ProcessEventsPayloads.RoomType.OPERATING_ROOM,
            ),
            dateTime = Instant.now().toString(),
        )
        val eventHandler = ProcessEventHandlers.PatientTrackedEventHandler(
            surgicalProcessController(),
            surgeryBookingController(),
            patientController(),
            medicalDeviceController(),
            MockEventProducer(),
        )
        eventHandler.canHandle(event) shouldBe true
    }

    "Medical Technology usage event should be handled correctly" {
        val event: Event<*> = ProcessEvent<ProcessEventsPayloads.MedicalTechnologyUsage>(
            key = ProcessEventsKeys.MEDICAL_TECHNOLOGY_USAGE_EVENT,
            data = ProcessEventsPayloads.MedicalTechnologyUsage(
                "mt-1",
                true,
            ),
            dateTime = Instant.now().toString(),
        )
        val eventHandler = ProcessEventHandlers.MedicalTechnologyUsageEventHandler(medicalDeviceController())
        eventHandler.canHandle(event) shouldBe true
    }

    "Medical Device usage event should be handled correctly" {
        val event: Event<*> = ProcessEvent<ProcessEventsPayloads.MedicalDeviceUsage>(
            key = ProcessEventsKeys.MEDICAL_DEVICE_USAGE_EVENT,
            data = ProcessEventsPayloads.MedicalDeviceUsage(
                "md-1",
                "prova-process",
            ),
            dateTime = Instant.now().toString(),
        )
        val eventHandler = ProcessEventHandlers.MedicalDeviceUsageEventHandler(medicalDeviceController())
        eventHandler.canHandle(event) shouldBe true
    }

    "Emergency surgery event should be handled correctly" {
        val event: Event<*> = ProcessEvent<ProcessEventsPayloads.EmergencySurgery>(
            key = ProcessEventsKeys.EMERGENCY_SURGERY_EVENT,
            data = ProcessEventsPayloads.EmergencySurgery(
                "room-1",
                "12345678",
            ),
            dateTime = Instant.now().toString(),
        )
        val eventHandler = ProcessEventHandlers.EmergencySurgeryEventHandler(
            surgicalProcessController(),
            patientController(),
        )
        eventHandler.canHandle(event) shouldBe true
    }
})
