package pwaula.trabalho.pizzariamario.s;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pwaula.trabalho.pizzariamario.s.model.ClientEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private ClientRepository clientRepository;

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    private ClientEntity getClientFromUser(UserEntity user) {
        return clientRepository.findClientEntityByUserId(user.getId());
    }

    public String getId() {
        return user.getId();
    }

    public String getName() {
        return getClientFromUser(user).getName();
    }

    public String getPhone() {
        return getClientFromUser(user).getPhone();
    }

    public String getAddress() {
        return getClientFromUser(user).getAddress();
    }

    public String getCpf() {
        return getClientFromUser(user).getCpf();
    }

    public String getCartId() {
        return getClientFromUser(user).getCartId();
    }

    public List<String> getOrdersDoneId() {
        return getClientFromUser(user).getOrdersDoneId();
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
