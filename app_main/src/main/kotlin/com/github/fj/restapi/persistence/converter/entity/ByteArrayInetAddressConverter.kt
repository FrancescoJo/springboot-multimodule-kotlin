/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.converter.entity

import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.net.toInetAddress
import com.github.fj.lib.net.toIpV6AddressBytes
import java.net.InetAddress
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class ByteArrayInetAddressConverter : AttributeConverter<InetAddress, ByteArray> {
    override fun convertToDatabaseColumn(attribute: InetAddress?): ByteArray =
            attribute?.toIpV6AddressBytes()
                    ?: InetAddressExtensions.EMPTY_INET_ADDRESS.toIpV6AddressBytes()

    override fun convertToEntityAttribute(dbData: ByteArray?): InetAddress =
            dbData?.toInetAddress() ?: InetAddressExtensions.EMPTY_INET_ADDRESS
}
