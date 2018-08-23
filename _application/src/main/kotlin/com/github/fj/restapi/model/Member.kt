/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.model

import com.github.fj.lib.util.EmptyObject
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Entity
@Table(name = "members")
data class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(unique = true, nullable = false)
        val id: Long,

        @Column(nullable = false)
        val name: String) {
    constructor() : this(0L, "")

    companion object : EmptyObject<Member> {
        override val EMPTY = Member()
    }
}
