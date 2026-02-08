package hr.abysalto.hiring.api.junior.dto.web;

import hr.abysalto.hiring.api.junior.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusForm {
    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus status;
}