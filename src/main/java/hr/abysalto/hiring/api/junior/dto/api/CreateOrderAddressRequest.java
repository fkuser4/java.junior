package hr.abysalto.hiring.api.junior.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderAddressRequest {
    @NotBlank
    private String city;

    @NotBlank
    private String street;

    private String homeNumber;
}