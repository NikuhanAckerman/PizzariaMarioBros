package pwaula.trabalho.pizzariamario.s.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pwaula.trabalho.pizzariamario.s.dto.*;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.*;
import pwaula.trabalho.pizzariamario.s.service.UserSessionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaInCartRepository pizzaInCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LocalOrderRepository localOrderRepository;

    @Autowired
    private LocalPizzaOrderedRepository localPizzaOrderedRepository;

    @GetMapping("")
    public ModelAndView controlPanel() {
        return new ModelAndView("controlpanel");
    }

    @GetMapping("/pizzas")
    public ModelAndView controlPanelPizza() {
        ModelAndView mv = new ModelAndView("pizzaPanel");
        mv.addObject("pizzas", pizzaRepository.findAll());
        return mv;
    }

    @GetMapping("/pizzas/criarPizza")
    public ModelAndView createNewPizza() {
        ModelAndView mv = new ModelAndView("createPizza");
        mv.addObject("pizzaDTO", new PizzaDTO());
        return mv;
    }

    @PostMapping("pizzas/criarPizza")
    public ModelAndView createNewPizzaPost(@ModelAttribute PizzaDTO pizzaDTO) {
        PizzaEntity pizza = new PizzaEntity(pizzaDTO.getName(),
                                            pizzaDTO.getDescription(),
                                            pizzaDTO.getPrice(),
                                            pizzaDTO.getImageUrl(),
                                            pizzaDTO.getIngredients());
        pizzaRepository.save(pizza);
        ModelAndView mv = new ModelAndView("redirect:/admin/pizzas");
        return mv;
    }

    @GetMapping("/pizzas/editarPizza/{pizzaId}")
    public ModelAndView editPizza(@PathVariable String pizzaId) {
        PizzaEntity pizza = pizzaRepository.findById(pizzaId).get();

        ModelAndView mv = new ModelAndView("updatePizza");
        mv.addObject("pizza", pizza);
        return mv;
    }

    @PutMapping("/pizzas/editarPizza/{pizzaId}")
    public ModelAndView editPizzaPut(@PathVariable String pizzaId, @ModelAttribute PizzaDTO pizzaDTO) {
        PizzaEntity pizza = pizzaRepository.findById(pizzaId).get();
        pizza.setName(pizzaDTO.getName());
        pizza.setDescription(pizzaDTO.getDescription());
        pizza.setPrice(pizzaDTO.getPrice());
        pizza.setImageUrl(pizzaDTO.getImageUrl());
        pizza.setIngredients(pizzaDTO.getIngredients());
        pizzaRepository.save(pizza);
        ModelAndView mv = new ModelAndView("redirect:/admin/pizzas");
        return mv;
    }

    @DeleteMapping("/pizzas/deletarPizza/{pizzaId}")
    public ModelAndView deletePizza(@PathVariable String pizzaId) {
        PizzaEntity pizza = pizzaRepository.findById(pizzaId).get();
        pizzaRepository.delete(pizza);
        return new ModelAndView("redirect:/admin/pizzas");
    }

    @GetMapping("/clientes")
    public ModelAndView controlPanelUsers() {
        ModelAndView mv = new ModelAndView("usersPanel");
        mv.addObject("users", userRepository.findAll());
        return mv;
    }

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

        List<OrderInDeliveryOrderPanelDTO> listOfOrderInDeliveryOrdersPanelDTO = new ArrayList<>();

        for(OrderDTO orderDTO : listOfOrdersDTO) {

            OrderInDeliveryOrderPanelDTO orderInDeliveryOrderPanelDTO = new OrderInDeliveryOrderPanelDTO(
                    orderDTO.getUserId(),
                    orderDTO.getOrderId(),
                    userRepository.findById(orderDTO.getUserId()).get().getName(),
                    userRepository.findById(orderDTO.getUserId()).get().getAddress(),
                    userRepository.findById(orderDTO.getUserId()).get().getPhone(),
                    userRepository.findById(orderDTO.getUserId()).get().getEmail(),
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
                return new ModelAndView("redirect:/admin/vendas/delivery");
            }
        }

        orderRepository.save(order);

        return new ModelAndView("redirect:/admin/vendas/delivery");
    }

    @GetMapping("/vendas/local")
    public ModelAndView localOrders(@RequestParam(defaultValue = "2025-01-01") LocalDate startDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();

        ModelAndView mv = new ModelAndView("localOrdersPanel");

        List<LocalOrderEntity> listOfLocalOrders = localOrderRepository.findLocalOrderEntitiesByOrderedAtTimeAfter(startDateTime);

        List<LocalOrderDTO> listOfLocalOrderDTOs = listOfLocalOrders.stream().map(localOrder -> new LocalOrderDTO(
                localOrder.getId(),
                localPizzaOrderedRepository.findAllById(localOrder.getPizzasOrderedList()).stream().map(localPizzaOrderedEntity -> new LocalPizzaOrderDTO(
                        localPizzaOrderedEntity.getPizzaId(),
                        pizzaRepository.findById(localPizzaOrderedEntity.getPizzaId()).get().getName(),
                        localPizzaOrderedEntity.getQuantityOrdered(),
                        localPizzaOrderedEntity.getIndividualPrice(),
                        localPizzaOrderedEntity.getTotalPrice()
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

        List<PizzaEntity> pizzas = pizzaRepository.findAll();
        mv.addObject("pizzas", pizzas);

        return mv;
    }

    @PostMapping("/vendas/local/criarPedidoLocal")
    public ModelAndView criarPedidoLocal(@RequestParam int mesa, @RequestParam Map<String, String> params) {

        LocalOrderEntity localOrderEntity = new LocalOrderEntity();

        // Set mesa
        localOrderEntity.setOrderTable(LocalOrderTable.valueOf("MESA_" + mesa));

        // Remove "mesa" from params so only pizza data remains
        params.remove("mesa");

        localOrderEntity.setOrderedAtTime(LocalDateTime.now());
        localOrderEntity.setTableFinishedAtTime(null);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String pizzaId = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());

            if (quantity > 0) {
                PizzaEntity pizza = pizzaRepository.findById(pizzaId).orElse(null);
                if (pizza == null) continue;

                LocalPizzaOrderedEntity ordered = new LocalPizzaOrderedEntity();
                ordered.setPizzaId(pizzaId);
                ordered.setQuantityOrdered(quantity);
                ordered.setIndividualPrice(pizza.getPrice());
                ordered.setTotalPrice(pizza.getPrice().multiply(BigDecimal.valueOf(quantity)));
                ordered.setLocalOrderId(localOrderEntity.getId());

                localPizzaOrderedRepository.save(ordered);
                localOrderEntity.getPizzasOrderedList().add(ordered.getId());
            }
        }

        List<BigDecimal> localPizzasOrderedTotalPrices = localOrderEntity.getPizzasOrderedList().stream()
                .map(localPizzaOrderedEntity -> localPizzaOrderedRepository.findById(localPizzaOrderedEntity).get().getTotalPrice()).toList();

        localOrderEntity.setTotalPrice(localPizzasOrderedTotalPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        localOrderRepository.save(localOrderEntity);

        return new ModelAndView("redirect:/admin/vendas/local");
    }

    @GetMapping("/vendas/local/atualizarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersAtualizarPedido(@PathVariable String localOrderId) {
        ModelAndView mv = new ModelAndView("updateLocalOrder");

        LocalOrderEntity localOrderEntity = localOrderRepository.findById(localOrderId).get();

        LocalOrderDTO newLocalOrderDTO = new LocalOrderDTO(
                localOrderEntity.getId(),
                localPizzaOrderedRepository.findAllById(localOrderEntity.getPizzasOrderedList()).stream()
                        .map(localPizzaOrderedEntity -> new LocalPizzaOrderDTO(
                                localPizzaOrderedEntity.getPizzaId(),
                                pizzaRepository.findById(localPizzaOrderedEntity.getPizzaId()).get().getName(),
                                localPizzaOrderedEntity.getQuantityOrdered(),
                                localPizzaOrderedEntity.getIndividualPrice(),
                                localPizzaOrderedEntity.getTotalPrice()
                        )).toList(),
                localOrderEntity.getOrderTable(),
                localOrderEntity.getOrderedAtTime(),
                localOrderEntity.getTableFinishedAtTime(),
                localOrderEntity.getTotalPrice()
        );

        List<PizzaEntity> pizzas = pizzaRepository.findAll();
        mv.addObject("pizzas", pizzas);
        mv.addObject("newLocalOrderDTO", newLocalOrderDTO);
        return mv;
    }

    @PutMapping("/vendas/local/atualizarPedidoLocal/{localOrderId}")
    public ModelAndView atualizarPedidoLocalmente(@PathVariable String localOrderId, @RequestParam int mesa, @RequestParam Map<String, String> params) {
        LocalOrderEntity localOrderEntity = localOrderRepository.findById(localOrderId).get();
        localOrderEntity.setPizzasOrderedList(new ArrayList<>());

        // Set mesa
        localOrderEntity.setOrderTable(LocalOrderTable.valueOf("MESA_" + mesa));

        // Remove "mesa" from params so only pizza data remains
        params.remove("mesa");
        params.remove("_method");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String pizzaId = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());

            if (quantity > 0) {
                PizzaEntity pizza = pizzaRepository.findById(pizzaId).orElse(null);
                if (pizza == null) continue;

                LocalPizzaOrderedEntity ordered = new LocalPizzaOrderedEntity();
                ordered.setPizzaId(pizzaId);
                ordered.setQuantityOrdered(quantity);
                ordered.setIndividualPrice(pizza.getPrice());
                ordered.setTotalPrice(pizza.getPrice().multiply(BigDecimal.valueOf(quantity)));
                ordered.setLocalOrderId(localOrderEntity.getId());

                localPizzaOrderedRepository.save(ordered);
                localOrderEntity.getPizzasOrderedList().add(ordered.getId());
            }
        }

        List<BigDecimal> localPizzasOrderedTotalPrices = localOrderEntity.getPizzasOrderedList().stream()
                .map(localPizzaOrderedEntity -> localPizzaOrderedRepository.findById(localPizzaOrderedEntity).get().getTotalPrice()).toList();

        localOrderEntity.setTotalPrice(localPizzasOrderedTotalPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

        localOrderRepository.save(localOrderEntity);

        return new ModelAndView("redirect:/admin/vendas/local");
    }

    @PutMapping("/vendas/local/terminarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersTerminarPedido(@PathVariable String localOrderId) {

        LocalOrderEntity localOrder = localOrderRepository.findById(localOrderId).get();

        if(localOrder.getTableFinishedAtTime() != null) { // nao atualizar o termino se ja  acabou o pedido
            return new ModelAndView("redirect:/admin/vendas/local");
        }

        localOrder.setTableFinishedAtTime(LocalDateTime.now());
        localOrderRepository.save(localOrder);

        return new ModelAndView("redirect:/admin/vendas/local");
    }

    @DeleteMapping("/vendas/local/deletarPedidoLocal/{localOrderId}")
    public ModelAndView localOrdersDeletarPedido(@PathVariable String localOrderId) {
        LocalOrderEntity localOrder = localOrderRepository.findById(localOrderId).get();
        localOrderRepository.delete(localOrder);
        return new ModelAndView("redirect:/admin/vendas/local");
    }


}
