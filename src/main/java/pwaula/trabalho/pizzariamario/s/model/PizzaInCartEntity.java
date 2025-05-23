package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "PizzaInCartEntity")
@Getter @Setter
public class PizzaInCartEntity {

    @Id
    private String id;

    private String pizzaId;

    private String cartId;

    private int quantityOrdered;

    private BigDecimal individualPrice;

    private BigDecimal totalPrice;

}
