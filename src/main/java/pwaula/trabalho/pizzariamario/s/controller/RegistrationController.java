package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.UserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

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
        user.setName(fullName);
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setAddress(address);
        user.setCpf(cpf);
        user.setRoles("USER");
        CartEntity cart = new CartEntity();
        cartRepository.save(cart);
        user.setCartId(cart.getId());

        userRepository.save(user);

        ModelAndView mv = new ModelAndView("redirect:/login");
        return mv;
    }

}
