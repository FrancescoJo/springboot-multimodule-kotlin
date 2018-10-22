/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.restapi.dto.AbstractResponseDto
import com.github.fj.restapi.dto.ErrorResponseDto
import org.springframework.http.HttpStatus

/**
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
class TestcaseResponse {
    private final HttpStatus status
    private final byte[] content

    protected TestcaseResponse(final HttpStatus status, final byte[] content) {
        this.status = status
        this.content = content
    }

    HttpStatus getStatus() {
        return status
    }

    byte[] getRawContent() {
        return Arrays.copyOf(content, content.length)
    }

    def <T> T parseOk(final Class<T> responseBodyType) {
        final rawResponseDto = new ObjectMapper().readValue(content, RawResponseDto.class)
        final body = rawResponseDto.body

        switch (rawResponseDto.type) {
            case "OK":
                return new ObjectMapper().treeToValue(body, responseBodyType)
            default:
                throw new IllegalArgumentException("Not a OK response: ${rawResponseDto.type}")
        }
    }

    def ErrorResponseDto parseError() {
        final rawResponseDto = new ObjectMapper().readValue(content, RawResponseDto.class)
        final body = rawResponseDto.body

        switch (rawResponseDto.type) {
            case "ERROR":
                return new AbstractResponseDto.Companion().error(
                        body.get("message").asText(),
                        body.get("reason").asText()
                )
            default:
                throw new IllegalArgumentException("Not a ERROR response: ${rawResponseDto.type}")
        }
    }

    /**
     * This class and its member variables should not be exposed to test case classes
     */
    protected static class Builder {
        int statusCode
        byte[] content

        TestcaseResponse build() {
            return new TestcaseResponse(HttpStatus.valueOf(statusCode), content)
        }
    }

    // All setters are used by JSON deserialiser
    @SuppressWarnings("unused")
    private static class RawResponseDto {
        private JsonNode body
        private String type

        JsonNode getBody() {
            return body
        }

        void setBody(JsonNode body) {
            this.body = body
        }

        String getType() {
            return type
        }

        void setType(String type) {
            this.type = type
        }
    }
}
