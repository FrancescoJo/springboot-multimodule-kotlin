/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.lib.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException

/**
 * Wrap your sensitive values, password, access token, etc. with this class to prevent it being
 * exposed to outside world via [toString] method, especially those properties of Kotlin data class.
 *
 * Note that containing value is declared as `var` for immediate removal of values from memory
 * (Although they will be cleared out after next GC, but setting a null on ineffective value
 * increases chance of stray values being purged on GC. For more explanations, read this
 * [Stack Overflow](https://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords)
 * post).
 *
 * Thanks [Mustafa Ali](https://developers.google.com/experts/people/mustafa-ali)'s brilliant idea
 * for this implementation.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
class ProtectedProperty<T> @JvmOverloads constructor(
        var value: T,
        private val scrubText: String = DEFAULT_SCRUB_TEXT
) {
    override fun toString() = scrubText

    companion object {
        private const val DEFAULT_SCRUB_TEXT = "[PROTECTED]"
    }

    class ProtectedPropertyJacksonSerialiser : StdSerializer<ProtectedProperty<*>>(ProtectedProperty::class.java) {
        override fun serialize(value: ProtectedProperty<*>, gen: JsonGenerator, serializers: SerializerProvider) {
            serializers.defaultSerializeValue(value.value, gen)
        }
    }

    class ProtectedPropertyJacksonDeserialiser : JsonDeserializer<ProtectedProperty<*>>(),
            ContextualDeserializer {
        private lateinit var valueType: JavaType

        override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
            return ProtectedPropertyJacksonDeserialiser().apply {
                this.valueType = property.type.containedType(0)
            }
        }

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedProperty<*> {
            return try {
                ProtectedProperty<Any>(ctxt.readValue(p, valueType))
            } catch (e: IOException) {
                throw UnsupportedOperationException("Unable to parse wrapped $valueType as JSON.", e)
            }
        }
    }
}
