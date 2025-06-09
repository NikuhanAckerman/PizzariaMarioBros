package pwaula.trabalho.pizzariamario.s.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwaula.trabalho.pizzariamario.s.model.CartEntity;
import pwaula.trabalho.pizzariamario.s.model.ProductInCartEntity;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductInCartRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    public void setCartTotal(CartEntity cart, List<ProductInCartEntity> cartItems) {
        BigDecimal total = new BigDecimal(0);

        total = cartItems.stream()
                .map(ProductInCartEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
    }

    public void setCartTotal(CartEntity cart, BigDecimal total) {
        cart.setTotalPrice(total);
    }

    public void clearCart(CartEntity cart) {

        if(!cart.getProductsInCartId().isEmpty()) {
            cart.getProductsInCartId().clear();
            productInCartRepository.deleteAll(productInCartRepository.getProductInCartEntitiesByCartId(cart.getId()));
            setCartTotal(cart, BigDecimal.valueOf(0));
            cartRepository.save(cart);
        }

    }

}
