package hr.abysalto.hiring.api.junior.dto.web;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemWebView {
    private Integer itemNr;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}