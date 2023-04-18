/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.event

import io.confluent.kafka.serializers.KafkaJsonDeserializer

/** Load the properties needed to initialize the Kafka consumer. */
fun loadConsumerProperties(bootstrapServerUrl: String, schemaRegistryUrl: String): Map<String, Any> = mapOf(
    "bootstrap.servers" to bootstrapServerUrl,
    "schema.registry.url" to schemaRegistryUrl,
    "group.id" to "surgical-process-consumer",
    "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" to KafkaJsonDeserializer::class.java,
)
