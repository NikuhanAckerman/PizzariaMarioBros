package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter @Setter
@Document(collection = "LocalPizzaOrderedEntity")
public class LocalProductOrderedEntity {

    @Id
    private String id;

    private String productId;

    private String localOrderId;

    private int quantityOrdered;

    private BigDecimal individualPrice;

    private BigDecimal totalPrice;

}
