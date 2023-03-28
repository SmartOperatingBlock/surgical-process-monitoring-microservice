/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import entity.process.SurgicalProcess

/** This interface models the operation on Digital Twins of [SurgicalProcess]. */
interface ProcessDigitalTwinManager {

    /**
     * Create the digital twin of the surgical [process].
     */
    fun createSurgicalProcess(process: SurgicalProcess): Boolean
}
