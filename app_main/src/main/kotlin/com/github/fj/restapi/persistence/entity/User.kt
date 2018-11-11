/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.persistence.entity

import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.text.SemanticVersion
import com.github.fj.lib.text.indentToString
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.EmptyObject
import com.github.fj.restapi.persistence.consts.account.LoginType
import com.github.fj.restapi.persistence.consts.account.PlatformType
import com.github.fj.restapi.persistence.consts.account.Role
import com.github.fj.restapi.persistence.consts.account.Status
import com.github.fj.restapi.persistence.converter.entity.*
import com.github.fj.restapi.vo.account.AccessToken
import java.io.Serializable
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

/**
 * Stores crucial user information for accessing service.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Entity
@Table(name = "users", uniqueConstraints = [
    UniqueConstraint(name = "UK_Users_Identity", columnNames = arrayOf("id", "id_token", "name")),
    UniqueConstraint(name = "UK_Users_Status", columnNames = arrayOf("id", "status")),
    UniqueConstraint(name = "UK_Users_Credential", columnNames = arrayOf("name", "login_type", "email", "credential"))
])
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    /**
     * Value for obscuring sequential property of primary key.
     */
    @Column(name = "id_token", length = 31, unique = true, nullable = false)
    var idToken: String = ""

    @Convert(converter = MemberStatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: Status = Status.UNDEFINED

    @Column(nullable = false, columnDefinition = "INT")
    var role: Role = Role.UNDEFINED

    @Column(name = "name", length = 31, unique = true, nullable = false)
    var name: String = ""

    @Convert(converter = LoginTypeConverter::class)
    @Column(name = "login_type", length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var loginType: LoginType = LoginType.UNDEFINED

    @Convert(converter = PlatformTypeConverter::class)
    @Column(name = "platform_type", length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var platformType: PlatformType = PlatformType.UNDEFINED

    @Column(name = "platform_version", length = 127, nullable = false)
    var platformVersion: String = ""

    @Convert(converter = SemanticVersionConverter::class)
    @Column(name = "app_version", length = 4, nullable = false, columnDefinition = "VARCHAR(31)")
    var appVersion: SemanticVersion = SemanticVersion.EMPTY

    @Column(length = EMAIL_LENGTH, nullable = false)
    var email: String = ""

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "created_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var createdIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Column(name = "push_token", nullable = false, columnDefinition = "TEXT")
    var pushToken: String = ""

    @Column(name = "invited_by", nullable = false)
    var invitedBy: Long = 0L

    @Column(columnDefinition = "VARBINARY(254)", nullable = false)
    var credential: ByteArray = ByteArray(0)

    @Convert(converter = AccessTokenEncodingConverter::class)
    @Column(name = "auth_encoding", length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var authEncoding: AccessToken.Encoded = AccessToken.Encoded.UNDEFINED

    @Column(name = "auth_iv", length = 16, nullable = false, columnDefinition = "VARBINARY(16)")
    var authIv: ByteArray = ByteArray(0)

    @Column(name = "access_token", length = 127, unique = true, nullable = false, columnDefinition = "VARBINARY(127)")
    var rawAccessToken: ByteArray = ByteArray(0)

    @Column(name = "token_issued_date", nullable = true)
    var tokenIssuedDate: LocalDateTime? = null

    @OneToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    var member: Membership = Membership.EMPTY

    override fun toString(): String {
        return "User(id=$id,\n" +
                "  idToken='$idToken',\n" +
                "  status=$status,\n" +
                "  role='$role',\n" +
                "  name='$name',\n" +
                "  loginType=$loginType,\n" +
                "  platformType=$platformType,\n" +
                "  platformVersion='$platformVersion',\n" +
                "  appVersion=$appVersion,\n" +
                "  email='$email',\n" +
                "  createdDate=$createdDate,\n" +
                "  createdIp=$createdIp,\n" +
                "  pushToken='$pushToken',\n" +
                "  invitedBy=$invitedBy,\n" +
                "  credential=${Arrays.toString(credential)},\n" +
                "  authEncoding=$authEncoding,\n" +
                "  authIv=${Arrays.toString(authIv)},\n" +
                "  rawAccessToken=${Arrays.toString(rawAccessToken)},\n" +
                "  tokenIssuedDate=$tokenIssuedDate,\n" +
                "  member=${indentToString(member)}" +
                ")"
    }

    fun setAccessToken(token: AccessToken) {
        authEncoding = token.mode
        authIv = token.iv.toByteArray()
        rawAccessToken = token.raw.toByteArray()
        tokenIssuedDate = token.issuedTimestamp
    }

    companion object : EmptyObject<User> {
        override val EMPTY = User()

        const val MINIMUM_NAME_LENGTH = 6
        const val MAXIMUM_NAME_LENGTH = 16

        // Originally could be maximum 254 but due to database restrictions
        const val EMAIL_LENGTH = 127
    }
}
