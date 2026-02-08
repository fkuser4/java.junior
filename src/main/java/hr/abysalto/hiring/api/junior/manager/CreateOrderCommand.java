package hr.abysalto.hiring.api.junior.manager;

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
public class CreateOrderCommand {
    @NotNull
    private Long buyerId;

    @NotNull
    private PaymentOption paymentOption;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    private String homeNumber;

    private String contactNumber;

    @Size(max = 500)
    private String note;

    @NotBlank
    private String currency;

    @NotEmpty
    @Valid
    private List<CreateOrderItemCommand> items = new ArrayList<>();
}