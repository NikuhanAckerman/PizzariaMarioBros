package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.ClientEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;
import pwaula.trabalho.pizzariamario.s.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ModelAndView register(@RequestParam String fullName,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String phone,
                                 @RequestParam String address,
                                 @RequestParam String cpf){

        UserEntity user = new UserEntity();
        ClientEntity client = new ClientEntity();

        ModelAndView mvErro = new ModelAndView("cadastrar");
        List<String> erros = new ArrayList<>();
        if (fullName == null || !fullName.matches("^[A-Za-zÀ-ÿ\\s]{1,40}$")) {
            erros.add("Nome inválido. Apenas letras e até 40 caracteres.");
        }

        if (password == null || !password.matches("^.{1,20}$")) {
            erros.add("Senha inválida. Deve ter até 20 caracteres.");
        }

        if (username == null || !username.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            erros.add("Email inválido. Deve conter '@' e '.'");
        }

        if (phone == null || !phone.matches("^\\d{1,9}$")) {
            erros.add("Telefone inválido. Apenas números e até 9 dígitos.");
        }

        if (address == null || !address.matches("^.{1,100}$")) {
            erros.add("Endereço inválido. Deve conter até 100 caracteres.");
        }

        if (cpf == null || !cpf.matches("^\\d{11}$")) {
            erros.add("CPF inválido. Deve conter exatamente 11 números.");
        }

        mvErro.addObject("erros", erros);

        if(!erros.isEmpty()){
            return mvErro;
        }

        client.setName(fullName);

        user.setEmail(username);
        client.setEmail(username);

        user.setPassword(passwordEncoder.encode(password));
        client.setPhone(phone);
        client.setAddress(address);
        client.setCpf(cpf);
        user.setRoles("USER");

        CartEntity cart = new CartEntity();
        cartRepository.save(cart);
        client.setCartId(cart.getId());

        userRepository.save(user);
        clientRepository.save(client);

        ModelAndView mv = new ModelAndView("redirect:/login");
        return mv;
    }

}
