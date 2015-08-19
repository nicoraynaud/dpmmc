package nc.noumea.mairie.dpmmc.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MockAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private DpmmcAuthoritiesPopulator _dpmmcAuthoritiesPopulator;

    private UserDetails user;

    public UserDetails getUser() {
        return user;
    }

    public MockAuthenticationProvider(DpmmcAuthoritiesPopulator authoritiesPopulator) {
        _dpmmcAuthoritiesPopulator = authoritiesPopulator;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        user = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return _dpmmcAuthoritiesPopulator.getGrantedAuthorities(null, usernamePasswordAuthenticationToken.getName());
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return usernamePasswordAuthenticationToken.getName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        return user;
    }
}
