package com.dilip.ccms.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank(message = "Street is required")
    private String street;
    @NotNull(message = "City is required")
    private Integer cityID;
    @NotNull(message = "State is required")
    private Integer stateID;
}
