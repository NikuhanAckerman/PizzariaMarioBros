package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.UserRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @PutMapping("/addPizza/{clientId}")
    public void addPizzaToCart(@RequestParam PizzaEntity pizzaEntity, @RequestParam int pizzaQuantity) {
        CartEntity cart = userSessionService.getUserEntity().getCart();

        PizzaInCartEntity pizzaInCartEntity = new PizzaInCartEntity();
        pizzaInCartEntity.setPizza(pizzaEntity);
        pizzaInCartEntity.setQuantityOrdered(pizzaQuantity);

        cart.getPizzasInCart().add(pizzaInCartEntity);
    }




}
