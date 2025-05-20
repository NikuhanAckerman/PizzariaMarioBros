package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "CartEntity")
@Getter @Setter
public class CartEntity {

    @Id
    private String id;

    List<PizzaInCartEntity> pizzasInCart = new ArrayList<>();

    private LocalDateTime timeFirstProductEntered;

}
