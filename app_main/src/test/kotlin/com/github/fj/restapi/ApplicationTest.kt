/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Oct - 2018
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest
class ApplicationTest {
    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun `Spring context loads successfully`() {
        assertNotNull(context)
    }
}
