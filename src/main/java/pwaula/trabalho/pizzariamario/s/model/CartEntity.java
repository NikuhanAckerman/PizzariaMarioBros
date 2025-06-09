package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "CartEntity")
@Getter @Setter
public class CartEntity {

    @Id
    private String id;

    private String clientId;

    private List<String> productsInCartId = new ArrayList<>();

    private BigDecimal totalPrice;

}
