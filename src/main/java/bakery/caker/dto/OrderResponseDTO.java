package bakery.caker.dto;

import bakery.caker.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDTO {
    List<Order> orderList;

    @Builder
    public OrderResponseDTO(List<Order> orderList){
        this.orderList = orderList;
    }
}
