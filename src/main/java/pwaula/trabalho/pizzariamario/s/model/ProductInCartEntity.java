package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "ProductInCartEntity")
@Getter @Setter
public class ProductInCartEntity {

    @Id
    private String id;

    private String productId;

    private String cartId;

    private int quantityOrdered;

    private BigDecimal individualPrice;

    private BigDecimal totalPrice;

}
