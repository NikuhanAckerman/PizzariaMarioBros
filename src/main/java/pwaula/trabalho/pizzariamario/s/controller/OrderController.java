package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pwaula.trabalho.pizzariamario.s.dto.ProductInCartDTO;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.*;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ProductRepository productRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/fazerPedido")
    public String makeOrder(RedirectAttributes redirectAttributes, Model model) {
        ClientEntity client = userSessionService.getClientEntity();
        ModelAndView mv = new ModelAndView();

        CartEntity cart = cartRepository.findById(client.getCartId()).get();

        if(cart.getProductsInCartId().isEmpty()) { // se nao tiver produto no carrinho, nem vai pra pagina de fazer pedido
            List<String> erros = new ArrayList<>();
            erros.add("Você não pode fazer um pedido sem produtos no carrinho.");
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/";
        }

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
                .toList();

        model.addAttribute("cart", cart);
        model.addAttribute("productsInCart", cartItems);

        return "order";
    }

    @PostMapping("/finalizarPedido")
    public String finalizeOrder(RedirectAttributes redirectAttributes) {
        System.out.println("CHAMOU A ROTA");

        ClientEntity client = userSessionService.getClientEntity();
        CartEntity cart = cartRepository.findById(client.getCartId()).get();

        List<ProductInCartEntity> productInCartList = productInCartRepository.getProductInCartEntitiesByCartId(cart.getId());

        if(productInCartList.isEmpty()) {
            return "redirect:/"; // sem produtos, de novo, nao precisa fazer o pedido
        }

        List<String> erros = new ArrayList<>();
        if(productInCartList.stream().anyMatch(productInCartEntity ->
                !productRepository.findById(productInCartEntity.getProductId()).get().isAvailableForProduction())) {
            erros.add("Existe(m) algum(ns) produto(s) indisponível(is) no carrinho.");
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/";
        }

        BigDecimal totalPriceOrder = productInCartList.stream()
                .map(ProductInCartEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = new OrderEntity();
        order.setClientId(client.getId());
        order.setTotalPrice(totalPriceOrder);
        order.setTimeOrderFinished(LocalDateTime.now());
        order.setStatus(OrderStatus.PEDIDO_RECEBIDO);
        order.setCartId(cart.getId());

        orderRepository.save(order);

        CartEntity newCart = new CartEntity();
        newCart.setClientId(client.getId());
        cartRepository.save(newCart);

        client.setCartId(newCart.getId());
        client.getOrdersDoneId().add(order.getId());
        clientRepository.save(client);

        return "redirect:/perfil";
    }


}
