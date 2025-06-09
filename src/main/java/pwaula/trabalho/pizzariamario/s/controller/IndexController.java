package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pwaula.trabalho.pizzariamario.s.dto.OrderDTO;
import pwaula.trabalho.pizzariamario.s.dto.ProductInCartDTO;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.OrderRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductInCartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductRepository;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping({"/index", "/"})
    public ModelAndView index() {

        List<ProductEntity> listOfPizzas = productRepository.findProductEntitiesByProductCategory(ProductCategory.PIZZAS);
        List<ProductEntity> listOfEsfihas = productRepository.findProductEntitiesByProductCategory(ProductCategory.ESFIHAS);
        List<ProductEntity> listOfBebidas = productRepository.findProductEntitiesByProductCategory(ProductCategory.BEBIDAS);
        List<ProductEntity> listOfAdicionais = productRepository.findProductEntitiesByProductCategory(ProductCategory.ADICIONAIS);

        ModelAndView mv = new ModelAndView("index");


        UserEntity user = userSessionService.getUserEntity();

        boolean canHaveControlPanelButton = user.getRoles().equals("ADMIN");
        boolean canHaveAtendenteUI = user.getRoles().equals("ATENDENTE");
        boolean canHaveClientUI = user.getRoles().equals("USER");

        mv.addObject("canHaveControlPanelButton", canHaveControlPanelButton);
        mv.addObject("canHaveAtendenteUI", canHaveAtendenteUI);
        mv.addObject("canHaveClientUI", canHaveClientUI);

        ClientEntity client = userSessionService.getClientEntity();

        if(client != null) {
            CartEntity cart = cartRepository.findById(client.getCartId()).get();

            List<ProductInCartEntity> productsInCart = productInCartRepository.findAllById(cart.getProductsInCartId());

            List<ProductInCartDTO> cartItems = productsInCart.stream()
                    .map(item -> new ProductInCartDTO(
                            item.getId(),
                            productRepository.findById(item.getProductId()).get().getName(),
                            productRepository.findById(item.getProductId()).get().getImageUrl(),
                            item.getIndividualPrice(),
                            item.getTotalPrice(),
                            item.getQuantityOrdered()
                    ))
                    .collect(Collectors.toList());

            System.out.println(cartItems);

            mv.addObject("userCart", cart);
            mv.addObject("cartItems", cartItems);
            mv.addObject("userName", client.getName());
        }

        mv.addObject("pizzas", listOfPizzas);
        mv.addObject("esfihas", listOfEsfihas);
        mv.addObject("bebidas", listOfBebidas);
        mv.addObject("adicionais", listOfAdicionais);

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

    @GetMapping("/404")
    public ModelAndView notFound() {
        ModelAndView mv = new ModelAndView("404");
        return mv;
    }

    @GetMapping("/405")
    public ModelAndView notAllowed() {
        ModelAndView mv = new ModelAndView("405");
        return mv;
    }

    @GetMapping("/perfil")
    public ModelAndView profile() {
        ClientEntity client = userSessionService.getClientEntity();

        ModelAndView mv = new ModelAndView();

        List<OrderEntity> listOfClientOrders = orderRepository.findAllById(client.getOrdersDoneId());

        List<OrderDTO> listOfUserOrdersDTO = listOfClientOrders.stream().map(order -> new OrderDTO(
                order.getClientId(),
                order.getId(),
                order.getStatus(),
                order.getTimeOrderFinished(),
                order.getTotalPrice(),
                cartRepository.findById(order.getCartId()).get().getProductsInCartId().stream().map(productInCartIds -> new ProductInCartDTO(
                        productInCartIds,
                        productRepository.findById(productInCartRepository.findById(productInCartIds).get().getProductId()).get().getName(),
                        productRepository.findById(productInCartRepository.findById(productInCartIds).get().getProductId()).get().getImageUrl(),
                        productInCartRepository.findById(productInCartIds).get().getIndividualPrice(),
                        productInCartRepository.findById(productInCartIds).get().getTotalPrice(),
                        productInCartRepository.findById(productInCartIds).get().getQuantityOrdered()
                )).toList()
        )).toList();

        System.out.println(listOfUserOrdersDTO);

        mv.setViewName("profile");
        mv.addObject("user", client);
        mv.addObject("orders", listOfUserOrdersDTO);


        return mv;
    }

}
