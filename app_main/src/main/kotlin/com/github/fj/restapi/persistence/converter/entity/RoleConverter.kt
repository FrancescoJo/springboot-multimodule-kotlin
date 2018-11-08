/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.restapi.persistence.consts.account.Role
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * Represents user's current role.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
@Converter
class RoleConverter : AttributeConverter<Role, Int> {
    override fun convertToDatabaseColumn(attribute: Role?): Int =
            attribute?.key ?: Role.UNDEFINED.key

    override fun convertToEntityAttribute(dbData: Int?): Role =
            Role.byKey(dbData)
}
