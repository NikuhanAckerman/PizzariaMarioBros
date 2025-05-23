package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.dto.PizzaInCartDTO;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.*;
import pwaula.trabalho.pizzariamario.s.service.CartService;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pedido")
public class OrderController {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaInCartRepository pizzaInCartRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fazerPedido")
    public ModelAndView makeOrder() {
        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

        if(cart.getPizzasInCartId().isEmpty()) { // se nao tiver produto no carrinho, nem vai pra pagina de fazer pedido
            return new ModelAndView("redirect:/");
        }

        List<PizzaInCartEntity> pizzasInCart = pizzaInCartRepository.findAllById(cart.getPizzasInCartId());

        List<PizzaInCartDTO> cartItems = pizzasInCart.stream()
                .map(item -> new PizzaInCartDTO(
                        item.getId(),
                        pizzaRepository.findById(item.getPizzaId()).get().getName(),
                        pizzaRepository.findById(item.getPizzaId()).get().getImageUrl(),
                        item.getIndividualPrice(),
                        item.getTotalPrice(),
                        item.getQuantityOrdered()
                ))
                .toList();

        ModelAndView mv = new ModelAndView("order");
        mv.addObject("cart", cart);
        mv.addObject("pizzasInCart", cartItems);

        return mv;
    }

    @PostMapping("/finalizarPedido")
    public ModelAndView finalizeOrder() {
        System.out.println("CHAMOU A ROTA");

        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

        List<PizzaInCartEntity> pizzaInCartList = pizzaInCartRepository.getPizzaInCartEntitiesByCartId(cart.getId());

        if(pizzaInCartList.isEmpty()) {
            return new ModelAndView("redirect:/"); // sem produtos, de novo, nao precisa fazer o pedido
        }

        BigDecimal totalPriceOrder = pizzaInCartList.stream()
                .map(PizzaInCartEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = new OrderEntity();
        order.setUserId(user.getId());
        order.setTotalPrice(totalPriceOrder);
        order.setTimeOrderFinished(LocalDateTime.now());
        order.setStatus(OrderStatus.PEDIDO_RECEBIDO);
        order.setCartId(cart.getId());

        orderRepository.save(order);

        CartEntity newCart = new CartEntity();
        newCart.setUserId(user.getId());
        cartRepository.save(newCart);

        user.setCartId(newCart.getId());
        user.getOrdersDoneId().add(order.getId());
        userRepository.save(user);

        return new ModelAndView("redirect:/perfil");
    }


}
