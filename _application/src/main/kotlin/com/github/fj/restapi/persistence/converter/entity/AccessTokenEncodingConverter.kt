/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.dto.account.AccessToken
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2018
 */
@Converter
class AccessTokenEncodingConverter : AttributeConverter<AccessToken.Encoded, String> {
    override fun convertToDatabaseColumn(attribute: AccessToken.Encoded?): String =
            attribute?.key ?: AccessToken.Encoded.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): AccessToken.Encoded =
            AccessToken.Encoded.byKey(dbData)
}
