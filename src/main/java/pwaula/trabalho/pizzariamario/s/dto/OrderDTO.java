package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pwaula.trabalho.pizzariamario.s.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class OrderDTO {
    private String userId;
    private String orderId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private BigDecimal cartTotalPrice;
    List<PizzaInCartDTO> pizzasInOrder;
}
