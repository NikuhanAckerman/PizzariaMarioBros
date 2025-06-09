package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductInCartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductRepository;
import pwaula.trabalho.pizzariamario.s.service.CartService;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrinho")
public class CartController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductInCartRepository productInCartRepository;

    @PostMapping("/adicionarProduto/{productId}")
    public String addProductToCart(@RequestParam int productQuantity,
                                       @PathVariable String productId,
                                         RedirectAttributes redirectAttributes) {
        List<String> erros = new ArrayList<>();

        ClientEntity client = userSessionService.getClientEntity();
        CartEntity cart = cartRepository.findById(client.getCartId()).get();
        ProductEntity product = productRepository.findById(productId).get();

        List<ProductInCartEntity> productsInCart = productInCartRepository.getProductInCartEntitiesByCartId(cart.getId());

        if(productsInCart.stream().
                anyMatch(
                productInCartEntity -> productInCartEntity.getProductId().equals(productId // a pizza ja esta no carrinho
                ))) {
            erros.add("O produto já está no carrinho.");
        }

        if(!product.isAvailableForProduction()) {
            erros.add("O produto está indisponível no momento.");
        }

        if(!erros.isEmpty()) {
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/";
        }

        ProductInCartEntity productInCartEntity = new ProductInCartEntity();
        productInCartEntity.setCartId(cart.getId());
        productInCartEntity.setProductId(productId);
        productInCartEntity.setQuantityOrdered(productQuantity);
        productInCartEntity.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(productQuantity)));
        productInCartEntity.setIndividualPrice(product.getPrice());
        productInCartRepository.save(productInCartEntity);

        cart.getProductsInCartId().add(productInCartEntity.getId());

        productsInCart = productInCartRepository.getProductInCartEntitiesByCartId(cart.getId());

        cartService.setCartTotal(cart, productsInCart);

        cartRepository.save(cart);

        return "redirect:/";
    }

    @PutMapping("/mudarQuantidadeProduto/{productInCartId}")
    public ModelAndView changeProductInCartQuantity(@RequestParam int newProductQuantity,
                                                  @PathVariable String productInCartId) {

        ClientEntity client = userSessionService.getClientEntity();
        CartEntity cart = cartRepository.findById(client.getCartId()).get();

        ProductInCartEntity productInCart = productInCartRepository.findById(productInCartId).get();

        productInCart.setQuantityOrdered(newProductQuantity);
        productInCart.setTotalPrice(productInCart.getIndividualPrice().multiply(BigDecimal.valueOf(newProductQuantity)));
        productInCartRepository.save(productInCart);

        List<ProductInCartEntity> productsInCart = productInCartRepository.getProductInCartEntitiesByCartId(cart.getId());
        cartService.setCartTotal(cart, productsInCart);
        cartRepository.save(cart);

        return new ModelAndView("redirect:/");
    }

    @DeleteMapping("/limparCarrinho")
    public String clearCart(RedirectAttributes redirectAttributes) {
        ClientEntity client = userSessionService.getClientEntity();
        CartEntity cart = cartRepository.findById(client.getCartId()).get();

        List<String> erros = new ArrayList<>();

        if(cart.getProductsInCartId().isEmpty()) { // o carrinho está vazio, não precisa tentar deletar
            erros.add("O carrinho já está vazio.");
            redirectAttributes.addFlashAttribute("erros", erros);
        } else {
            cart.getProductsInCartId().clear();
            productInCartRepository.deleteAll(productInCartRepository.getProductInCartEntitiesByCartId(cart.getId()));
            cartService.setCartTotal(cart, BigDecimal.valueOf(0));
            cartRepository.save(cart);
        }

        return "redirect:/";
    }

    @DeleteMapping("/tirarProduto/{productInCartId}")
    public String removePizzaFromCart(@PathVariable String productInCartId,
                                            RedirectAttributes redirectAttributes) {
        ClientEntity client = userSessionService.getClientEntity();
        CartEntity cart = cartRepository.findById(client.getCartId()).get();

        List<String> erros = new ArrayList<>();

        if(!cart.getProductsInCartId().contains(productInCartId)) { // a pizza deletada não está no
            erros.add("O carrinho já está vazio.");
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/";
        }

        ProductInCartEntity productInCart = productInCartRepository.findById(productInCartId).get();
        productInCartRepository.delete(productInCart);

        List<ProductInCartEntity> productsInCart = productInCartRepository.getProductInCartEntitiesByCartId(cart.getId());
        cartService.setCartTotal(cart, productsInCart);
        cartRepository.save(cart);

        return "redirect:/";
    }

}
