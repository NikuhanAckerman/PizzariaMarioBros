package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.dto.PizzaDTO;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private PizzaRepository pizzaRepository;

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

        System.out.println(listOfPizzaDTOs);

        ModelAndView mv = new ModelAndView("index");
        mv.addObject("pizzas", listOfPizzaDTOs);
        return mv;
    }
}
