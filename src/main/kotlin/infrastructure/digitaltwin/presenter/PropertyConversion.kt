/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.presenter

/** Utility module for digital twins operations. */
object PropertyConversion {

    /**
     * Obtain a Digital Twin property and convert it to its [T] type.
     * In case it is null or the conversion is not possible then get a [defaultValue].
     */
    inline fun <reified T> Any?.propertyAs(defaultValue: T): T =
        when (this) {
            is T -> this
            else -> defaultValue
        }
}
