/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.appconfig

import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.fj.lib.util.ProtectedProperty

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
class JacksonDeserialisationModule : SimpleModule(JacksonDeserialisationModule::class.java.canonicalName) {
    init {
        addSerializer(ProtectedProperty.ProtectedPropertyJacksonSerialiser())

        addDeserializer(ProtectedProperty::class.java,
                ProtectedProperty.ProtectedPropertyJacksonDeserialiser())
    }
}
