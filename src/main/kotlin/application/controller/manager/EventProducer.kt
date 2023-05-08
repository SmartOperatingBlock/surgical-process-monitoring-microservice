/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import application.presenter.event.model.Event

/**
 * The [Event] producer.
 */
fun interface EventProducer {

    /** Produce the [event] into an event broker. */
    fun produceEvent(event: Event<*>)
}
