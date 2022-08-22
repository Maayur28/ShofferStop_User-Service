package com.userservice.service;

import org.json.JSONObject;

import com.userservice.entity.UserEntity;
import com.userservice.model.request.UserCreateAddressRequest;
import com.userservice.model.request.UserUpdateRequest;
import com.userservice.model.response.Account;
import com.userservice.model.response.UserCreateAddressResponse;
import com.userservice.model.response.UserCreateAddressResponseDTO;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.model.response.UserUpdateResponse;
import com.userservice.shared.dto.UserDto;

public interface UserService {
	UserCreateResponse createUser(UserDto user) throws Exception;
	
	JSONObject getUser(String token);

	UserEntity loadUserByUsername(String username);

	Account getAccount(String token);
	
	UserCreateAddressResponse getUserAddress(String token, int page, int pageSize);

	UserCreateAddressResponse createUserAddress(UserCreateAddressRequest userAddress, String token, int page,
			int pageSize) throws Exception;

	UserCreateAddressResponse deleteAddress(String token, int page, int pageSize, String addressId);

	UserCreateAddressResponse updateAddress(String token, int page, int pageSize, String addressId,
			UserCreateAddressResponseDTO addressRequest);

	UserCreateAddressResponse updateDefaultAddress(String token, int page, int pageSize, String addressId);

	UserUpdateResponse userUpdateRequest(String token, UserUpdateRequest userUpdateRequest);

	UserCreateAddressResponse getUserSearchAddress(String token, String search, int page, int pageSize);
}
