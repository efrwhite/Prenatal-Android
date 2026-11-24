package com.elizabethwhitebaker.prenatal.session

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalSerializationApi::class)
@Serializable(SessionSerializer::class)
@KeepGeneratedSerializer
data class Session(
    val uuid: Uuid,
    val title: String,
    @SerialName(SessionSerializer.PAGES_SERIAL_NAME) // Redundant in theory, but ensures consistency
    val pages: List<SessionPage>
)

// Transforms ArticleBlock type discriminators from standalone super-keys to inline values under a pre-set key
object SessionSerializer : JsonTransformingSerializer<Session>(Session.generatedSerializer()) {
    const val PAGES_SERIAL_NAME = "pages"
    const val BLOCKS_SERIAL_NAME = "blocks"
    const val BLOCK_DISCRIMINATOR = "#class"

    override fun transformDeserialize(element: JsonElement): JsonElement {
        try {
            // Convert the top level article to be mutable, so we can replace its pages
            val articleObject = element.jsonObject.toMutableMap()

            // Get the "pages" array out of the top level article
            val pagesArray = articleObject[PAGES_SERIAL_NAME]!!.jsonArray
            // Transform the array and all contained blocks into a serializable layout
            articleObject[PAGES_SERIAL_NAME] =
                JsonArray(transformObjectArray(pagesArray, BLOCKS_SERIAL_NAME))

            // Return the updated article to ultimately deserialize
            return JsonObject(articleObject)
        }
        // TODO check which can be thrown...?
        catch (_: Exception) {
            return element
        }
    }

    // This iterate-and-transform logic is required multiple times, so it's split to its own function
    fun transformObjectArray(array: JsonArray, blocksKey: String): JsonArray {
        // Get the array out of the top level element, and convert it to be mutable
        val mutableArray = array.toMutableList()
        // Iterate over the array with in-place mutation
        val arrayIterator = mutableArray.listIterator()
        while (arrayIterator.hasNext()) {
            // Get the sub-element itself and convert it to be mutable
            val mutableSubElement = arrayIterator.next().jsonObject.toMutableMap()
            // Get the blocks array out of the sub-element
            val blocksArray = mutableSubElement[blocksKey]!!.jsonArray
            // Replace the blocks array in the sub-element with one the deserializer will deserialize
            mutableSubElement[blocksKey] = transformPageBlockArray(blocksArray)
            // Replace the page in the array with its mutated version
            arrayIterator.set(JsonObject(mutableSubElement))
        }
        return JsonArray(mutableArray)
    }

    fun transformPageBlockArray(elements: JsonArray): JsonArray {
        try {
            val transformedElements = mutableListOf<JsonElement>()

            for (blockElement in elements) {
                val pageBlock = blockElement.jsonObject.values.first().jsonObject.toMutableMap()
                val blockType = blockElement.jsonObject.keys.first()
                pageBlock[BLOCK_DISCRIMINATOR] = JsonPrimitive(blockType)
                if (blockType == "list" || blockType == "showIfSaved") {
                    // Since these blocks have their elements top-level, we can just transform them in one-shot
                    pageBlock["elements"] =
                        transformPageBlockArray(pageBlock["elements"]!!.jsonArray)
                } else if (blockType == "accordion") {
                    // Accordion has both a top-level block array and an object array that itself contains blocks...
                    pageBlock["defaultElements"] =
                        transformPageBlockArray(pageBlock["defaultElements"]!!.jsonArray)
                    // ...so make sure to account for both
                    pageBlock["choices"] =
                        transformObjectArray(pageBlock["choices"]!!.jsonArray, "elements")
                }
                transformedElements.add(JsonObject(pageBlock))
            }

            return JsonArray(transformedElements)
        }
        // TODO check which can be thrown...?
        catch (_: Exception) {
            return elements
        }
    }
}