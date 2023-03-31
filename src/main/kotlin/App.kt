import infrastructure.api.ApiRouter
import infrastructure.event.KafkaClient
import infrastructure.provider.ManagerProviderImpl

/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

/**
 * The launcher of the Surgical Process Monitoring System Microservice.
 */
fun main() {
    val provider = ManagerProviderImpl()
    ApiRouter(provider).start()
    KafkaClient(provider).start()
}
