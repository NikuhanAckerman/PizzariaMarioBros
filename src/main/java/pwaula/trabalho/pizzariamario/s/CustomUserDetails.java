package pwaula.trabalho.pizzariamario.s;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.OrderEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getAddress() {
        return user.getAddress();
    }

    public String getCpf() {
        return user.getCpf();
    }

    public String getCartId() {
        return user.getCartId();
    }

    public List<String> getOrdersDoneId() {
        return user.getOrdersDoneId();
    }

    public String roles() {
        return user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRoles()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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

}
