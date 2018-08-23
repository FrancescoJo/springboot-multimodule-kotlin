/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * All deserialisation logic inheriting this class must have a default constructor.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class JsonRequestDeserializer<T> : JsonDeserializer<T>() {
    abstract fun createObject(jsonNode: JsonNode): T

    abstract fun createEmpty(): T

    final override fun deserialize(p: JsonParser, context: DeserializationContext): T {
        return try {
            createObject(p.codec.readTree(p))
        } catch (t: IOException) {
            LOG.warn("Unparsable request {}", t.toString())
            createEmpty()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JsonRequestDeserializer::class.java)
    }
}
