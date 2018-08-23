/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.endpoint.hello

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.util.EmptyObject
import com.github.fj.restapi.dto.JsonRequestDeserializer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@JsonDeserialize(using = HelloRequestDto::class)
data class HelloRequestDto @JvmOverloads constructor(val name: String = "") :
        JsonRequestDeserializer<HelloRequestDto>() {
    override fun createObject(jsonNode: JsonNode) = with(jsonNode) {
        HelloRequestDto(name = get("name")?.textValue() ?: "")
    }

    override fun createEmpty() = EMPTY

    companion object : EmptyObject<HelloRequestDto> {
        override val EMPTY = HelloRequestDto("")
    }
}
