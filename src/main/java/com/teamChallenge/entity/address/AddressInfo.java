package com.teamChallenge.entity.address;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfo {

    @NotBlank
    private String firstName, lastName, phoneNumber, country, state, city, address, postalCode;
}
