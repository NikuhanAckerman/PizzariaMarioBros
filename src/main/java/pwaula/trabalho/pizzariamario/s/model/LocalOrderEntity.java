package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "LocalOrderEntity")
@Getter @Setter
public class LocalOrderEntity {

    @Id
    private String id;

    private List<String> productsOrderedList = new ArrayList<>();

    private LocalOrderTable orderTable;

    private LocalDateTime orderedAtTime;

    private LocalDateTime tableFinishedAtTime;

    private BigDecimal totalPrice;

}
