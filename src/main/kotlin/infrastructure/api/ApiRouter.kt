/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import infrastructure.api.routes.processAPI
import infrastructure.provider.ManagerProvider
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import java.time.format.DateTimeParseException

/**
 * The API router of the application.
 */
class ApiRouter(private val provider: ManagerProvider) {

    private val version = "v1"
    private val apiPath: String = "/api/$version"

    companion object {
        /** The port of the KTor server. */
        const val NETTY_PORT = 3000
    }

    /** Start the Ktor server. */
    fun start() {
        embeddedServer(Netty, port = NETTY_PORT) {
            module(this)
        }.start(wait = false)
    }

    /** The KTor Application module. */
    fun module(app: Application) {
        with(app) {
            configureRouting()
            configureSerialization()
            exceptionHandler()
        }
    }

    /**
     * Extension function to configure routing management.
     */
    private fun Application.configureRouting() {
        routing {
            processAPI(apiPath, provider)
        }
    }

    /**
     * Extension function to configure serialization management.
     */
    private fun Application.configureSerialization() {
        install(ContentNegotiation) {
            json()
        }
    }

    private fun Application.exceptionHandler() {
        install(StatusPages) {
            exception<DateTimeParseException> { call, _ ->
                call.respondText(
                    text = "Date time information must be in ISO 8601 format",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
    }
}
