package pwaula.trabalho.pizzariamario.s.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pwaula.trabalho.pizzariamario.s.ClientDetails;
import pwaula.trabalho.pizzariamario.s.model.ClientEntity;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;


public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientEntity client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new ClientDetails(client);
    }


}
