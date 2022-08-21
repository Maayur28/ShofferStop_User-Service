package com.userservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateAddressRequest {
	@NonNull
	private String country;
	@NonNull
	private String fullName;
	@NonNull
	private Integer pincode;
	@NonNull
	private String mobile;
	@NonNull
	private String houseAddress;
	@NonNull
	private String state;
	@NonNull
	private String city;
	
	private boolean defaultAddress;
}
