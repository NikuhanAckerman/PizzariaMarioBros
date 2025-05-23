package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class LocalPizzaOrderDTO {
    private String pizzaId;
    private String pizzaName;
    private int quantityOrdered;
    private BigDecimal individualPrice;
    private BigDecimal totalPrice;
}
