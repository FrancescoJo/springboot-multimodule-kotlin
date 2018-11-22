/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package test.com.github.fj.restapi.account;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2018
 */
public class MockAuthentication extends AbstractAuthenticationToken {
    private Object credential;
    private Object principal;

    public MockAuthentication() {
        this(Collections.emptyList());
    }

    public MockAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    public void setCredentials(final Object value) {
        this.credential = value;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(final Object value) {
        this.principal = value;
    }
}
