/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.UserActivity
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Nov - 2018
 */
@Converter
class UserActivityConverter : AttributeConverter<UserActivity, String> {
    override fun convertToDatabaseColumn(attribute: UserActivity?): String =
            attribute?.key ?: UserActivity.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: String?): UserActivity =
            UserActivity.byKey(dbData)
}
