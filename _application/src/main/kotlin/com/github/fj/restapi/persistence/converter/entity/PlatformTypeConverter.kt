/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.account.PlatformType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class PlatformTypeConverter : AttributeConverter<PlatformType, String> {
    override fun convertToDatabaseColumn(attribute: PlatformType?): String =
            attribute?.key ?: PlatformType.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): PlatformType =
            PlatformType.byKey(dbData)
}
