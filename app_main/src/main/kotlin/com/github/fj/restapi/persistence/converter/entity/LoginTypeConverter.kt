/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.account.LoginType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class LoginTypeConverter : AttributeConverter<LoginType, String> {
    override fun convertToDatabaseColumn(attribute: LoginType?): String =
            attribute?.key ?: LoginType.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): LoginType =
            LoginType.byKey(dbData)
}
