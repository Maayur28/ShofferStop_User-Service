package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserCreateAddressResponse {
    private List<UserCreateAddressResponseDTO> addresses;
    private Pagination pagination;
    private long total;
}
