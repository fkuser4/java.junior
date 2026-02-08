package hr.abysalto.hiring.api.junior.dto.api;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Integer itemNr;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}