/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.account.Gender
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class GenderConverter : AttributeConverter<Gender, String> {
    override fun convertToDatabaseColumn(attribute: Gender?): String =
            attribute?.key ?: Gender.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): Gender =
            Gender.byKey(dbData)
}
