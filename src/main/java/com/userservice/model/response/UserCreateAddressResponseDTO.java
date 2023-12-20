package com.userservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateAddressResponseDTO {
    private String addressId;
    private String country;
    private String fullName;
    private Integer pincode;
    private String mobile;
    private String houseAddress;
    private String state;
    private String city;
    private Integer defaultAddress;
}
