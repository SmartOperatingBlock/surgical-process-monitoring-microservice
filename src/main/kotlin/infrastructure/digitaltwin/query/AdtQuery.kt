/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.digitaltwin.query

/**
 * Helper class to construct query for Azure Digital Twins service.
 * @param[query] is the start state of the query.
 */
class AdtQuery private constructor(val query: String) {
    /** Select query considering returned [elements]. */
    fun select(vararg elements: String) = AdtQuery("SELECT ${elements.joinToString(", ")}")

    /** Select query but return only top [numberOfElementsToSelect] [elements].  */
    fun selectTop(numberOfElementsToSelect: Int, vararg elements: String) =
        AdtQuery("SELECT TOP($numberOfElementsToSelect) ${elements.joinToString(", ")}")

    /** Specify the FROM digital twins clause with its [alias]. This alias can be used later to refer to it. */
    fun fromDigitalTwins(alias: String) = AdtQuery("$query FROM DIGITALTWINS $alias")

    /**
     * Join the query with a relationship, specified by its [relationshipName] of the [srcAlias] digital twin.
     * Specify also the [dstAlias] used to describe the destination digital twin.
     */
    fun joinRelationship(dstAlias: String, srcAlias: String, relationshipName: String) =
        AdtQuery("$query JOIN $dstAlias RELATED $srcAlias.$relationshipName")

    /** [whereClause] to include in the query. */
    fun where(whereClause: String) = AdtQuery("$query WHERE $whereClause")

    /** Chain in and a [whereClause]. */
    fun and(whereClause: String) = AdtQuery("$query AND $whereClause")

    companion object {
        /** Start a new query. */
        fun createQuery(): AdtQuery = AdtQuery("")

        object AdtQueryUtils {
            /**
             * Utility to specify the model check on a digital twin inside the where clause.
             * Usage: alias isOfModel [model]
             */
            infix fun String.isOfModel(model: String): String = "IS_OF_MODEL($this, '$model')"

            /**
             * Utility to specify an equality check on a digital twin inside the where clause.
             * Usage: alias.property = [element]
             */
            inline infix fun <reified T> String.eq(element: T): String =
                "$this = " + when (element) {
                    is String -> "'$element'"
                    else -> element.toString()
                }
        }
    }
}
