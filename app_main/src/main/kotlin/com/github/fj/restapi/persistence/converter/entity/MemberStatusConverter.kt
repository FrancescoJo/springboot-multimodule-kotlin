/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.account.Status
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class MemberStatusConverter : AttributeConverter<Status, String> {
    override fun convertToDatabaseColumn(attribute: Status?): String =
            attribute?.key ?: Status.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): Status =
            Status.byKey(dbData)
}
