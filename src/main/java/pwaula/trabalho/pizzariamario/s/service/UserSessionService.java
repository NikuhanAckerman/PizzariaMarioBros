package pwaula.trabalho.pizzariamario.s.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pwaula.trabalho.pizzariamario.s.CustomUserDetails;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.UserRepository;

@Service
public class UserSessionService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetails getLoggedUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Object principal = auth.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return (CustomUserDetails) principal;
            }
        }
        return null;
    }

    public UserEntity getUserEntity() {
        CustomUserDetails user = getLoggedUserDetails();
        return user != null ? userRepository.findByEmail(user.getUsername()) : null;
    }

}
