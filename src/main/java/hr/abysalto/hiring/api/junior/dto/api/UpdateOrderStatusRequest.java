package hr.abysalto.hiring.api.junior.dto.api;

import hr.abysalto.hiring.api.junior.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}