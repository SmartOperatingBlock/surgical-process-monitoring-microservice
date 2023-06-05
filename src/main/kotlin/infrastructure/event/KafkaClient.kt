/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.event

import application.controller.MedicalDeviceController
import application.controller.PatientDataController
import application.controller.SurgeryBookingController
import application.controller.SurgicalProcessController
import application.controller.manager.EventProducer
import application.handler.EventHandler
import application.handler.PatientEventHandlers
import application.handler.ProcessEventHandlers
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessEventsKeys
import application.presenter.event.serialization.EventSerialization.toEvent
import com.fasterxml.jackson.databind.ObjectMapper
import infrastructure.provider.ManagerProvider
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.format.DateTimeParseException

/** The Kafka Client necessary to consume surgical process events.
 */
class KafkaClient(private val provider: ManagerProvider) : EventProducer {

    private val eventHandlers: List<EventHandler>

    init {
        listOf(System.getenv("BOOTSTRAP_SERVER_URL"), System.getenv("SCHEMA_REGISTRY_URL")).forEach {
            requireNotNull(it) {
                """
                Invalid environment variable: $it
                Check out the documentation here:
                https://github.com/SmartOperatingBlock/bootstrap"""
                    .trimIndent()
            }
        }

        val medicalDeviceController = MedicalDeviceController(
            provider.medicalDeviceDatabaseManager,
            provider.medicalDeviceDigitalTwinManager,
        )

        val surgicalProcessController = SurgicalProcessController(
            provider.processDatabaseManager,
            provider.processDigitalTwinManager,
        )

        val patientDataController = PatientDataController(
            provider.patientMedicalDataDatabaseManager,
            provider.patientDigitalTwinManager,
        )

        val surgeryBookingController = SurgeryBookingController(provider.surgeryBookingDigitalTwinManager)

        eventHandlers = listOf(
            ProcessEventHandlers.MedicalDeviceUsageEventHandler(medicalDeviceController),
            ProcessEventHandlers.MedicalTechnologyUsageEventHandler(medicalDeviceController),
            PatientEventHandlers.PatientOnOperatingTableEventHandler(surgicalProcessController),
            PatientEventHandlers.BodyTemperatureUpdateEventHandler(patientDataController),
            PatientEventHandlers.DiastolicPressureUpdateEventHandler(patientDataController),
            PatientEventHandlers.SystolicPressureUpdateEventHandler(patientDataController),
            PatientEventHandlers.RespiratoryRateUpdateEventHandler(patientDataController),
            PatientEventHandlers.SaturationUpdateEventHandler(patientDataController),
            PatientEventHandlers.HeartbeatUpdateEventHandler(patientDataController),
            ProcessEventHandlers.PatientTrackedEventHandler(
                surgicalProcessController,
                surgeryBookingController,
                patientDataController,
                medicalDeviceController,
                this,
            ),
            ProcessEventHandlers.EmergencySurgeryEventHandler(surgicalProcessController, patientDataController),
            ProcessEventHandlers.ProcessManualEventHandler(
                surgicalProcessController,
                surgeryBookingController,
                patientDataController,
                medicalDeviceController,
            ),
        )
    }

    private val kafkaConsumer: KafkaConsumer<String, String> = KafkaConsumer(
        loadConsumerProperties(
            System.getenv("BOOTSTRAP_SERVER_URL"),
            System.getenv("SCHEMA_REGISTRY_URL"),
        ),
    )

    private val kafkaProducer: KafkaProducer<String, Event<*>> = KafkaProducer(
        loadProducerProperties(
            System.getenv("BOOTSTRAP_SERVER_URL"),
            System.getenv("SCHEMA_REGISTRY_URL"),
        ),
    )

    /** Start consuming the events on the Kafka Broker. */
    fun start() {
        kafkaConsumer.subscribe(
            listOf(
                processEventsTopic,
                emergencyEventsTopic,
                processManualEventsTopic,
            ),
        ).run {
            while (true) {
                kafkaConsumer.poll(Duration.ofMillis(pollingTime)).forEach { event ->
                    try {
                        consumeEvent(event)
                    } catch (e: IllegalArgumentException) {
                        println("Error: Invalid Event Schema. Event discarded! - $e")
                    } catch (e: DateTimeParseException) {
                        println("Error: Invalid Date in Event. Event discarded! - $e")
                    }
                }
            }
        }
    }

    private fun consumeEvent(event: ConsumerRecord<String, String>) {
        val deserializedEvent = ObjectMapper().writeValueAsString(event.value()).toEvent(event.key())
        this@KafkaClient.eventHandlers
            .filter {
                it.canHandle(deserializedEvent)
            }
            .forEach {
                it.consume(deserializedEvent)
            }
    }

    companion object {
        /** The polling time. */
        private const val pollingTime: Long = 100L
        private const val processEventsTopic = "process-events"
        private const val emergencyEventsTopic = "emergency-surgery-events"
        private const val surgeryReportTopic = "process-summary-events"
        private const val processManualEventsTopic = "process-manual-events"
    }

    override fun produceEvent(event: Event<*>) {
        when (event.key) {
            ProcessEventsKeys.SURGERY_REPORT_EVENT -> {
                val record = ProducerRecord(surgeryReportTopic, event.key, event)
                kafkaProducer.send(record)
            }
        }
    }
}
