package com.userservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateAddressResponse {
    private List<UserCreateAddressResponseDTO> addresses;
    private Pagination pagination;
    private long total;
}
