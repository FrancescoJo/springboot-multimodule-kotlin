/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.lib.text.SemanticVersion
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class SemanticVersionConverter : AttributeConverter<SemanticVersion, String> {
    override fun convertToDatabaseColumn(attribute: SemanticVersion?): String =
            attribute?.toString() ?: SemanticVersion.EMPTY.toString()

    override fun convertToEntityAttribute(dbData: String?): SemanticVersion =
            SemanticVersion.parse(dbData)
}
