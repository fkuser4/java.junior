package hr.abysalto.hiring.api.junior.dto.api;

import hr.abysalto.hiring.api.junior.model.PaymentOption;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long buyerId;

    @NotNull
    private PaymentOption paymentOption;

    @NotNull
    @Valid
    private CreateOrderAddressRequest deliveryAddress;

    private String contactNumber;

    @Size(max = 500)
    private String note;

    @NotBlank
    private String currency;

    @NotEmpty
    @Valid
    private List<CreateOrderItemRequest> items = new ArrayList<>();
}