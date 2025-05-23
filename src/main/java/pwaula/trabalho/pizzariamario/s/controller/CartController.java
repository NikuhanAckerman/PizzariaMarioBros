package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaInCartRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;
import pwaula.trabalho.pizzariamario.s.service.CartService;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/carrinho")
public class CartController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    PizzaInCartRepository pizzaInCartRepository;

    @PostMapping("/adicionarPizza/{pizzaId}")
    public ModelAndView addPizzaToCart(@RequestParam int pizzaQuantity,
                                       @PathVariable String pizzaId) {
        if(pizzaQuantity > 10) { // nao pode pedir mais que 10 pizzas
            return new ModelAndView("redirect:/");
        }

        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();
        PizzaEntity pizza = pizzaRepository.findById(pizzaId).get();

        List<PizzaInCartEntity> pizzasInCart = pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId());

        if(pizzasInCart.stream().
                anyMatch(
                pizzaInCartEntity -> pizzaInCartEntity.getPizzaId().equals(pizzaId // a pizza ja esta no carrinho
                ))) {
            return new ModelAndView("redirect:/");
        }

        PizzaInCartEntity pizzaInCartEntity = new PizzaInCartEntity();
        pizzaInCartEntity.setCartId(cart.getId());
        pizzaInCartEntity.setPizzaId(pizzaId);
        pizzaInCartEntity.setQuantityOrdered(pizzaQuantity);
        pizzaInCartEntity.setTotalPrice(pizza.getPrice().multiply(BigDecimal.valueOf(pizzaQuantity)));
        pizzaInCartEntity.setIndividualPrice(pizza.getPrice());
        pizzaInCartRepository.save(pizzaInCartEntity);

        cart.getPizzasInCartId().add(pizzaInCartEntity.getId());

        pizzasInCart = pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId());

        cartService.setCartTotal(cart, pizzasInCart);

        cartRepository.save(cart);


        return new ModelAndView("redirect:/");
    }

    @PutMapping("/mudarQuantidadePizza/{pizzaInCartId}")
    public ModelAndView changePizzaInCartQuantity(@RequestParam int newPizzaQuantity,
                                                  @PathVariable String pizzaInCartId) {
        if(newPizzaQuantity > 10) { // nao pode pedir mais que 10 pizzas
            return new ModelAndView("redirect:/");
        }

        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

        PizzaInCartEntity pizzaInCart = pizzaInCartRepository.findById(pizzaInCartId).get();

        pizzaInCart.setQuantityOrdered(newPizzaQuantity);
        pizzaInCart.setTotalPrice(pizzaInCart.getIndividualPrice().multiply(BigDecimal.valueOf(newPizzaQuantity)));
        pizzaInCartRepository.save(pizzaInCart);

        List<PizzaInCartEntity> pizzasInCart = pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId());
        cartService.setCartTotal(cart, pizzasInCart);
        cartRepository.save(cart);

        return new ModelAndView("redirect:/");
    }

    @DeleteMapping("/limparCarrinho")
    public ModelAndView clearCart() {
        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

        if(cart.getPizzasInCartId().isEmpty()) { // o carrinho está vazio, não precisa tentar deletar
            return new ModelAndView("redirect:/");
        } else {
            cart.getPizzasInCartId().clear();
            pizzaInCartRepository.deleteAll(pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId()));
            cartService.setCartTotal(cart, BigDecimal.valueOf(0));
            cartRepository.save(cart);
        }

        return new ModelAndView("redirect:/");
    }

    @DeleteMapping("/tirarPizza/{pizzaInCartId}")
    public ModelAndView removePizzaFromCart(@PathVariable String pizzaInCartId) {
        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

        if(!cart.getPizzasInCartId().contains(pizzaInCartId)) { // a pizza deletada não está no carrinho
            return new ModelAndView("redirect:/");
        }

        PizzaInCartEntity pizzaInCart = pizzaInCartRepository.findById(pizzaInCartId).get();
        pizzaInCartRepository.delete(pizzaInCart);

        List<PizzaInCartEntity> pizzasInCart = pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId());
        cartService.setCartTotal(cart, pizzasInCart);
        cartRepository.save(cart);

        return new ModelAndView("redirect:/");
    }

}
