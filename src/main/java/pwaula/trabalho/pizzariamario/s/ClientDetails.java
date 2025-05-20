package pwaula.trabalho.pizzariamario.s;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pwaula.trabalho.pizzariamario.s.model.ClientEntity;

import java.util.Collection;
import java.util.Collections;

public class ClientDetails implements UserDetails {

    private final ClientEntity client;

    public ClientDetails(ClientEntity client) {
        this.client = client;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // No roles for now
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getEmail(); // Use email as username
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public ClientEntity getClient() {
        return client;
    }

}
