package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PizzaInCartEntity")
@Getter @Setter
public class PizzaInCartEntity {

    @Id
    private String id;

    private PizzaEntity pizza;

    private int quantityOrdered;





}
