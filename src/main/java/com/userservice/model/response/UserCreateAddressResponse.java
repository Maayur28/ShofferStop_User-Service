package com.userservice.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateAddressResponse {
	private List<UserCreateAddressResponseDTO> addresses;
	private Pagination pagination;
	private long total;
}
