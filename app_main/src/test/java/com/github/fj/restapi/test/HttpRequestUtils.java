/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.restapi.test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
public final class HttpRequestUtils {
    public static HttpServletRequest newMockHttpServletRequestByLocalhost() {
        final HttpServletRequest mocked = mock(HttpServletRequest.class);
        when(mocked.getRemoteAddr()).thenReturn("localhost");

        return mocked;
    }
}
