package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.CustomUserDetails;
import pwaula.trabalho.pizzariamario.s.dto.OrderDTO;
import pwaula.trabalho.pizzariamario.s.dto.PizzaDTO;
import pwaula.trabalho.pizzariamario.s.dto.PizzaInCartDTO;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.OrderRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaInCartRepository;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PizzaInCartRepository pizzaInCartRepository;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping({"/index", "/"})
    public ModelAndView index() {

        List<PizzaEntity> listOfPizzas = pizzaRepository.findAll();

        ModelAndView mv = new ModelAndView("index");

        if(userSessionService.getUserEntity().getRoles().equals("ADMIN")) {
            mv.addObject("canHaveControlPanelButton", true);
        } else {
            mv.addObject("canHaveControlPanelButton", false);
        }

        UserEntity user = userSessionService.getUserEntity();
        CartEntity cart = cartRepository.findById(user.getCartId()).get();

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
                .collect(Collectors.toList());

        System.out.println(cartItems);

        mv.addObject("pizzas", listOfPizzas);
        mv.addObject("userCart", cart);
        mv.addObject("cartItems", cartItems);
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

    @GetMapping("/403")
    public ModelAndView forbidden() {
        ModelAndView mv = new ModelAndView("403");
        return mv;
    }

    @GetMapping("/perfil")
    public ModelAndView profile() {
        UserEntity user = userSessionService.getUserEntity();

        List<OrderEntity> listOfUserOrders = orderRepository.findAllById(user.getOrdersDoneId());

        List<OrderDTO> listOfUserOrdersDTO = listOfUserOrders.stream().map(order -> new OrderDTO(
                order.getUserId(),
                order.getId(),
                order.getStatus(),
                order.getTimeOrderFinished(),
                order.getTotalPrice(),
                cartRepository.findById(order.getCartId()).get().getPizzasInCartId().stream().map(pizzaInCartIds -> new PizzaInCartDTO(
                        pizzaInCartIds,
                        pizzaRepository.findById(pizzaInCartRepository.findById(pizzaInCartIds).get().getPizzaId()).get().getName(),
                        pizzaRepository.findById(pizzaInCartRepository.findById(pizzaInCartIds).get().getPizzaId()).get().getImageUrl(),
                        pizzaRepository.findById(pizzaInCartRepository.findById(pizzaInCartIds).get().getPizzaId()).get().getPrice(),
                        pizzaInCartRepository.findById(pizzaInCartIds).get().getTotalPrice(),
                        pizzaInCartRepository.findById(pizzaInCartIds).get().getQuantityOrdered()
                )).toList()
        )).toList();

        System.out.println(listOfUserOrdersDTO);

        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("user", user);
        mv.addObject("orders", listOfUserOrdersDTO);

        return mv;
    }

}
