package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.CustomUserDetails;
import pwaula.trabalho.pizzariamario.s.dto.PizzaDTO;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private UserSessionService userSessionService;

    @GetMapping({"/index", "/"})
    public ModelAndView index() {

        List<PizzaEntity> listOfPizzas = pizzaRepository.findAll();
        List<PizzaDTO> listOfPizzaDTOs = new ArrayList<PizzaDTO>();

        for (PizzaEntity pizza : listOfPizzas) {
            PizzaDTO pizzaDTO = new PizzaDTO();
            pizzaDTO.setName(pizza.getName());
            pizzaDTO.setDescription(pizza.getDescription());
            pizzaDTO.setPrice(pizza.getPrice());
            pizzaDTO.setImageUrl(pizza.getImageUrl());
            pizzaDTO.setIngredients(pizza.getIngredients());
            listOfPizzaDTOs.add(pizzaDTO);
        }

        ModelAndView mv = new ModelAndView("index");

        if(userSessionService.getUserEntity().getRoles().equals("ADMIN")) {
            mv.addObject("canHaveControlPanelButton", true);
        } else {
            mv.addObject("canHaveControlPanelButton", false);
        }

        mv.addObject("pizzas", listOfPizzaDTOs);
        mv.addObject("userName", userSessionService.getUserEntity().getName());

        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView("cadastrar");
        return mv;
    }

}
