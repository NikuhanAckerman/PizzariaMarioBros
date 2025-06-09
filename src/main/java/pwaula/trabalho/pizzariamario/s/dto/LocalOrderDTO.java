package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwaula.trabalho.pizzariamario.s.model.LocalOrderTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalOrderDTO {
    private String id;
    private List<LocalProductOrderDTO> productsOrderedList;
    private LocalOrderTable orderTable;
    private LocalDateTime orderedAtTime;
    private LocalDateTime tableFinishedAtTime;
    private BigDecimal totalPrice;
}
