/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api.util

import kotlinx.serialization.Serializable

/** The API response in case of multiple data in API response.
 *  The API returns a list of [entries] and the [total] number of elements . */
@Serializable
data class ResponseEntryList<out T>(val entries: List<T>, val total: Int = entries.count())
