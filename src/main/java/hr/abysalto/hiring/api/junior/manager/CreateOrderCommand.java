package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.PaymentOption;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderCommand {
    private Long buyerId;
    private PaymentOption paymentOption;
    private String city;
    private String street;
    private String homeNumber;
    private String contactNumber;
    private String note;
    private String currency;
    private List<CreateOrderItemCommand> items = new ArrayList<>();
}