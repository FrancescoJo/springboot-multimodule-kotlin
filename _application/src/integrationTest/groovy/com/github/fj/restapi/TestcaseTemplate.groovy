/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.util.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TestcaseTemplate extends Specification {
    @Autowired
    private WebApplicationContext context

    private MockMvc mvc

    def setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    protected TestcaseResponse get(final String url, final Pair<String, Object>... params = null) {
        final builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
        if (params != null) {
            params.each {
                if (it.first() == null || it.toString().isEmpty()) {
                    return
                }

                String value
                if (it.second() == null) {
                    value = ""
                } else {
                    value = it.second().toString()
                }

                builder.param(it.first(), value)
            }
        }

        final result = mvc.perform(builder)

        return buildTcResponse(result)
    }

    protected TestcaseResponse post(final String url, final Object requestObject) {
        final result = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestObject)))

        return buildTcResponse(result)
    }

    private static TestcaseResponse buildTcResponse(final ResultActions result) {
        final builder = new TestcaseResponse.Builder()

        result.andDo({ mvcResult ->
            builder.statusCode = mvcResult.response.status
            builder.content = mvcResult.response.contentAsByteArray
        })

        return builder.build()
    }

    private static String asJsonString(final Object obj) {
        return new ObjectMapper().writeValueAsString(obj)
    }
}
