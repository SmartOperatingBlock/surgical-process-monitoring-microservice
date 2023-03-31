/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api.routes

import application.controller.SurgicalProcessController
import application.service.SurgicalProcessServices
import infrastructure.api.util.ResponseEntryList
import infrastructure.provider.ManagerProvider
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

/** The API route of Surgical Process Monitoring System Microservice. */
fun Route.processAPI(apiPath: String, provider: ManagerProvider) {

    get("$apiPath/processes") {
        SurgicalProcessServices.GetCurrentSurgicalProcesses(
            SurgicalProcessController(
                provider.processDatabaseManager,
                provider.processDigitalTwinManager
            )
        ).execute().toList().run {
            call.response.status(if (this.isEmpty()) HttpStatusCode.NoContent else HttpStatusCode.OK)
            call.respond(ResponseEntryList(this))
        }
    }
}
