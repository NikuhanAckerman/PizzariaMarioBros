package pwaula.trabalho.pizzariamario.s.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter @Setter @ToString
public class PizzaDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String ingredients;
}
