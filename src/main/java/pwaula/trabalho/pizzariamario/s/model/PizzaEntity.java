package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Document(collection = "PizzaEntity")
@Getter @Setter
public class PizzaEntity {

    @Id
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private String ingredients;

    public PizzaEntity(String name,
                       String description,
                       BigDecimal price,
                       String imageUrl,
                       String ingredients) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
    }

}
