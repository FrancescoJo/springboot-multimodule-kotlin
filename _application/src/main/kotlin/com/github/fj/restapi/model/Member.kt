/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.model

import com.github.fj.lib.util.EmptyObject
import java.io.Serializable
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Entity
@Table(name = "members")
data class Member @JvmOverloads constructor(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(unique = true, nullable = false)
        val id: Long = 0L,

        @Column(nullable = false)
        val name: String = "") : Serializable {
    companion object : EmptyObject<Member> {
        override val EMPTY = Member()
    }
}
