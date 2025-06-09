package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class ProductInCartDTO {
    private String productInCartId;
    private String productName;
    private String productImageUrl;
    private BigDecimal individualPrice;
    private BigDecimal totalPrice;
    private int quantityOrdered;
}
