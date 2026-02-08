package hr.abysalto.hiring.api.junior.dto.web;

import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.model.PaymentOption;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OrderWebView {
    private Long orderId;
    private String buyerName;
    private OrderStatus orderStatus;
    private String orderStatusLabel;
    private LocalDateTime orderTime;
    private PaymentOption paymentOption;
    private String paymentOptionLabel;
    private String deliveryAddress;
    private String contactNumber;
    private String note;
    private String currency;
    private BigDecimal totalPrice;
    private List<OrderItemWebView> items = new ArrayList<>();
}