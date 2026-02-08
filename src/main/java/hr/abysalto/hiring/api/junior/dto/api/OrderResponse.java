package hr.abysalto.hiring.api.junior.dto.api;

import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.model.PaymentOption;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private Long buyerId;
    private OrderStatus orderStatus;
    private LocalDateTime orderTime;
    private PaymentOption paymentOption;
    private Long deliveryAddressId;
    private String contactNumber;
    private String note;
    private String currency;
    private BigDecimal totalPrice;
    private List<OrderItemResponse> items = new ArrayList<>();
}