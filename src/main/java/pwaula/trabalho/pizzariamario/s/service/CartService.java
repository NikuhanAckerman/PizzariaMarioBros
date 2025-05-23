package pwaula.trabalho.pizzariamario.s.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.PizzaInCartEntity;
import pwaula.trabalho.pizzariamario.s.model.UserEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaInCartRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PizzaInCartRepository pizzaInCartRepository;

    public void setCartTotal(CartEntity cart, List<PizzaInCartEntity> cartItems) {
        BigDecimal total = new BigDecimal(0);

        total = cartItems.stream()
                .map(PizzaInCartEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
    }

    public void setCartTotal(CartEntity cart, BigDecimal total) {
        cart.setTotalPrice(total);
    }

    public void clearCart(CartEntity cart) {

        if(!cart.getPizzasInCartId().isEmpty()) {
            cart.getPizzasInCartId().clear();
            pizzaInCartRepository.deleteAll(pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId()));
            setCartTotal(cart, BigDecimal.valueOf(0));
            cartRepository.save(cart);
        }

    }

}
