package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pwaula.trabalho.pizzariamario.s.dto.*;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/atendente")
public class AttendantController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductInCartRepository productInCartRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LocalProductOrderedRepository localProductOrderedRepository;

    @Autowired
    LocalOrderRepository localOrderRepository;

    @GetMapping("/vendas")
    public ModelAndView controlPanelSales() {
        ModelAndView mv = new ModelAndView("salesControlPanel");

        return mv;
    }

    @GetMapping("/vendas/delivery")
    public ModelAndView deliveryOrders() {
        ModelAndView mv = new ModelAndView("deliveryOrdersPanel");

        List<OrderEntity> listOfOrders = orderRepository.findAll();

        List<OrderDTO> listOfOrdersDTO = listOfOrders.stream().map(order -> new OrderDTO(
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

        List<OrderInDeliveryOrderPanelDTO> listOfOrderInDeliveryOrdersPanelDTO = new ArrayList<>();

        for(OrderDTO orderDTO : listOfOrdersDTO) {

            OrderInDeliveryOrderPanelDTO orderInDeliveryOrderPanelDTO = new OrderInDeliveryOrderPanelDTO(
                    orderDTO.getClientId(),
                    orderDTO.getOrderId(),
                    clientRepository.findById(orderDTO.getClientId()).get().getName(),
                    clientRepository.findById(orderDTO.getClientId()).get().getAddress(),
                    clientRepository.findById(orderDTO.getClientId()).get().getPhone(),
                    clientRepository.findById(orderDTO.getClientId()).get().getEmail(),
                    orderDTO
            );

            listOfOrderInDeliveryOrdersPanelDTO.add(orderInDeliveryOrderPanelDTO);

        }

        mv.addObject("orders", listOfOrderInDeliveryOrdersPanelDTO);

        return mv;
    }

    @PutMapping("/vendas/delivery/mudarStatus/{orderId}")
    public ModelAndView deliveryMudarStatus(@PathVariable String orderId, @RequestParam int status) {
        OrderEntity order = orderRepository.findById(orderId).get();

        switch (status) {
            case 1 -> order.setStatus(OrderStatus.PEDIDO_RECEBIDO);
            case 2 -> order.setStatus(OrderStatus.PIZZA_SENDO_FEITA);
            case 3 -> order.setStatus(OrderStatus.DELIVERY_A_CAMINHO);
            case 4 -> order.setStatus(OrderStatus.DELIVERY_CONCLUIDO);
            case 5 -> order.setStatus(OrderStatus.DELIVERY_CANCELADO);
            default -> {
                return new ModelAndView("redirect:/atendente/vendas/delivery");
            }
        }

        orderRepository.save(order);

        return new ModelAndView("redirect:/atendente/vendas/delivery");
    }

    @GetMapping("/vendas/local")
    public ModelAndView localOrders(@RequestParam(defaultValue = "2025-01-01") LocalDate startDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();

        ModelAndView mv = new ModelAndView("localOrdersPanel");

        List<LocalOrderEntity> listOfLocalOrders = localOrderRepository.findLocalOrderEntitiesByOrderedAtTimeAfter(startDateTime);

        List<LocalOrderDTO> listOfLocalOrderDTOs = listOfLocalOrders.stream().map(localOrder -> new LocalOrderDTO(
                localOrder.getId(),
                localProductOrderedRepository.findAllById(localOrder.getProductsOrderedList()).stream().map(localProductOrderedEntity -> new LocalProductOrderDTO(
                        localProductOrderedEntity.getProductId(),
                        productRepository.findById(localProductOrderedEntity.getProductId()).get().getName(),
                        localProductOrderedEntity.getQuantityOrdered(),
                        localProductOrderedEntity.getIndividualPrice(),
                        localProductOrderedEntity.getTotalPrice()
                )).toList(),
                localOrder.getOrderTable(),
                localOrder.getOrderedAtTime(),
                localOrder.getTableFinishedAtTime(),
                localOrder.getTotalPrice()
        )).toList();

        mv.addObject("orders", listOfLocalOrderDTOs);

        return mv;
    }

    @GetMapping("/vendas/local/criarPedidoLocal")
    public ModelAndView localOrdersCriarPedido() {
        ModelAndView mv = new ModelAndView("createLocalOrder");

        List<ProductEntity> products = productRepository.findAll();
        mv.addObject("products", products);

        return mv;
    }

    @PostMapping("/vendas/local/criarPedidoLocal")
    public String criarPedidoLocal(@RequestParam int mesa, @RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {

        LocalOrderEntity localOrderEntity = new LocalOrderEntity();

        // Set mesa
        localOrderEntity.setOrderTable(LocalOrderTable.valueOf("MESA_" + mesa));

        // Remove "mesa" from params so only pizza data remains
        params.remove("mesa");

        localOrderEntity.setOrderedAtTime(LocalDateTime.now());
        localOrderEntity.setTableFinishedAtTime(null);

        List<String> erros = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String productId = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());

            if (quantity > 0) {
                ProductEntity product = productRepository.findById(productId).orElse(null);

                if (product == null) continue;

                if (!product.isAvailableForProduction()) {
                    erros.add("O produto " + product.getName() + " está indisponível atualmente.");
                }

                LocalProductOrderedEntity ordered = new LocalProductOrderedEntity();
                ordered.setProductId(productId);
                ordered.setQuantityOrdered(quantity);
                ordered.setIndividualPrice(product.getPrice());
                ordered.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                ordered.setLocalOrderId(localOrderEntity.getId());

                localProductOrderedRepository.save(ordered);
                localOrderEntity.getProductsOrderedList().add(ordered.getId());
            }
        }

        if(!erros.isEmpty()) {
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/atendente/vendas/local/criarPedidoLocal";
        }

        List<BigDecimal> localProductsOrderedTotalPrices = localOrderEntity.getProductsOrderedList().stream()
                .map(localPizzaOrderedEntity -> localProductOrderedRepository.findById(localPizzaOrderedEntity).get().getTotalPrice()).toList();

        localOrderEntity.setTotalPrice(localProductsOrderedTotalPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        localOrderRepository.save(localOrderEntity);

        return "redirect:/atendente/vendas/local";
    }

    @GetMapping("/vendas/local/atualizarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersAtualizarPedido(@PathVariable String localOrderId) {
        ModelAndView mv = new ModelAndView("updateLocalOrder");

        LocalOrderEntity localOrderEntity = localOrderRepository.findById(localOrderId).get();

        LocalOrderDTO newLocalOrderDTO = new LocalOrderDTO(
                localOrderEntity.getId(),
                localProductOrderedRepository.findAllById(localOrderEntity.getProductsOrderedList()).stream()
                        .map(localProductOrderedEntity -> new LocalProductOrderDTO(
                                localProductOrderedEntity.getProductId(),
                                productRepository.findById(localProductOrderedEntity.getProductId()).get().getName(),
                                localProductOrderedEntity.getQuantityOrdered(),
                                localProductOrderedEntity.getIndividualPrice(),
                                localProductOrderedEntity.getTotalPrice()
                        )).toList(),
                localOrderEntity.getOrderTable(),
                localOrderEntity.getOrderedAtTime(),
                localOrderEntity.getTableFinishedAtTime(),
                localOrderEntity.getTotalPrice()
        );

        List<ProductEntity> products = productRepository.findAll();
        mv.addObject("products", products);
        mv.addObject("newLocalOrderDTO", newLocalOrderDTO);
        return mv;
    }

    @PutMapping("/vendas/local/atualizarPedidoLocal/{localOrderId}")
    public String atualizarPedidoLocalmente(@PathVariable String localOrderId, @RequestParam int mesa, @RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        LocalOrderEntity localOrderEntity = localOrderRepository.findById(localOrderId).get();
        localOrderEntity.setProductsOrderedList(new ArrayList<>());

        // Set mesa
        localOrderEntity.setOrderTable(LocalOrderTable.valueOf("MESA_" + mesa));

        // Remove "mesa" from params so only pizza data remains
        params.remove("mesa");
        params.remove("_method");

        List<String> erros = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String productId = entry.getKey();
            System.out.println(entry.getKey() + " " + entry.getValue());
            int quantity = Integer.parseInt(entry.getValue());

            if (quantity > 0) {
                ProductEntity product = productRepository.findById(productId).orElse(null);
                if (product == null) continue;

                if (!product.isAvailableForProduction()) {
                    erros.add("O produto " + product.getName() + " está indisponível atualmente.");
                }

                LocalProductOrderedEntity ordered = new LocalProductOrderedEntity();
                ordered.setProductId(productId);
                ordered.setQuantityOrdered(quantity);
                ordered.setIndividualPrice(product.getPrice());
                ordered.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                ordered.setLocalOrderId(localOrderEntity.getId());

                localProductOrderedRepository.save(ordered);
                localOrderEntity.getProductsOrderedList().add(ordered.getId());
            }
        }

        if(!erros.isEmpty()) {
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/atendente/vendas/local/criarPedidoLocal";
        }

        List<BigDecimal> localProductsOrderedTotalPrices = localOrderEntity.getProductsOrderedList().stream()
                .map(localPizzaOrderedEntity -> localProductOrderedRepository.findById(localPizzaOrderedEntity).get().getTotalPrice()).toList();

        localOrderEntity.setTotalPrice(localProductsOrderedTotalPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        localOrderRepository.save(localOrderEntity);

        return "redirect:/atendente/vendas/local";
    }

    @PutMapping("/vendas/local/terminarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersTerminarPedido(@PathVariable String localOrderId) {

        LocalOrderEntity localOrder = localOrderRepository.findById(localOrderId).get();

        if(localOrder.getTableFinishedAtTime() != null) { // nao atualizar o termino se ja  acabou o pedido
            return new ModelAndView("redirect:/atendente/vendas/local");
        }

        localOrder.setTableFinishedAtTime(LocalDateTime.now());
        localOrderRepository.save(localOrder);

        return new ModelAndView("redirect:/atendente/vendas/local");
    }

    @DeleteMapping("/vendas/local/deletarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersDeletarPedido(@PathVariable String localOrderId) {
        LocalOrderEntity localOrder = localOrderRepository.findById(localOrderId).get();
        localOrderRepository.delete(localOrder);
        return new ModelAndView("redirect:/atendente/vendas/local");
    }

}
