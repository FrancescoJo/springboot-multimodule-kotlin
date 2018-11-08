/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fj.restapi.dto.OkResponseDto;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.com.github.fj.restapi.appconfig.ApplicationContextHolder;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
public final class HttpTransportUtils {
    public static HttpServletRequest newMockHttpServletRequestByLocalhost() {
        final HttpServletRequest mocked = mock(HttpServletRequest.class);
        when(mocked.getRemoteAddr()).thenReturn("localhost");

        return mocked;
    }

    public static <T> T expect2xxBody(final WebTestClient.ResponseSpec response,
                                      final Class<T> expectedType) {
        final OkResponseDto<?> okResponse = response.expectStatus().is2xxSuccessful()
                .expectBody(OkResponseDto.class).returnResult().getResponseBody();

        if (okResponse == null) {
            throw new AssertionError("Response contains no content body!");
        }

        final ObjectMapper defaultMapper = ApplicationContextHolder
                .getInstance().getApplicationContext().getBean(ObjectMapper.class);

        return defaultMapper.convertValue(okResponse.getBody(), expectedType);
    }
}
