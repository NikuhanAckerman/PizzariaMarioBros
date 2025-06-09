package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class OrderInDeliveryOrderPanelDTO {
    private String clientId;
    private String orderId;
    private String userOrderedName;
    private String userOrderedAddress;
    private String userOrderedPhone;
    private String userOrderedEmail;
    private OrderDTO order;
}
