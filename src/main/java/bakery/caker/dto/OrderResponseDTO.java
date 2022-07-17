package bakery.caker.dto;

import bakery.caker.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    List<Order> orderList;
}
