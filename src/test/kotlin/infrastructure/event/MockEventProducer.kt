/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.event

import application.controller.manager.EventProducer
import application.presenter.event.model.Event

class MockEventProducer : EventProducer {

    override fun produceEvent(event: Event<*>) = println("Producing event.. $event")
}
