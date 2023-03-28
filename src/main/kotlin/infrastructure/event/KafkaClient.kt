/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.event

import application.controller.MedicalDeviceController
import application.controller.SurgicalProcessController
import application.handler.EventHandler
import application.handler.EventHandlers
import application.presenter.event.serialization.EventSerialization.toEvent
import com.fasterxml.jackson.databind.ObjectMapper
import infrastructure.provider.ManagerProvider
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.time.format.DateTimeParseException

/** The Kafka Client necessary to consume surgical process events.
 */
class KafkaClient(private val provider: ManagerProvider) {

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
            provider.medicalDeviceDigitalTwinManager
        )

        val surgicalProcessController = SurgicalProcessController(
            provider.processDatabaseManager,
            provider.processDigitalTwinManager
        )
//        val surgeryBookingController = SurgeryBookingController(provider.surgeryBookingDigitalTwinManager)
//
//        val patientDataController = PatientDataController(provider.patientMedicalDataDatabaseManager)

        eventHandlers = listOf(
            EventHandlers.MedicalDeviceUsageEventHandler(medicalDeviceController),
            EventHandlers.MedicalTechnologyUsageEventHandler(medicalDeviceController),
            EventHandlers.ProcessInfoEventHandler(surgicalProcessController)
        )
    }

    private val kafkaConsumer: KafkaConsumer<String, String> = KafkaConsumer(
        loadConsumerProperties(
            System.getenv("BOOTSTRAP_SERVER_URL"),
            System.getenv("SCHEMA_REGISTRY_URL")
        )
    )

    /** Start consuming the events on the Kafka Broker. */
    fun start() {
        kafkaConsumer.subscribe(listOf(processEventsTopic)).run {
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
    }
}
