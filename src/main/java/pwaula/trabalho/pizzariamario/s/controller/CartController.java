package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @PutMapping("/addPizza/{clientId}")
    public void addPizzaToCart(@RequestParam PizzaEntity pizzaEntity, @RequestParam int pizzaQuantity, @PathVariable String clientId) {
        PizzaInCartEntity pizzaInCartEntity = new PizzaInCartEntity();
        pizzaInCartEntity.setPizza(pizzaEntity);
        pizzaInCartEntity.setQuantityOrdered(pizzaQuantity);

        CartEntity cart = clientRepository.findById(clientId).get().getCart();

        cart.getPizzasInCart().add(pizzaInCartEntity);
    }




}
