package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "OrderEntity")
@Getter @Setter
public class OrderEntity {

    @Id
    private String id;

    private String clientId;

    private String cartId;

    private LocalDateTime timeOrderFinished;

    private BigDecimal totalPrice;

    private OrderStatus status;

}
