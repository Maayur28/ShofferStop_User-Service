package com.userservice.service;

import org.json.JSONObject;

import com.userservice.entity.UserEntity;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.shared.dto.UserDto;

public interface UserService {
	UserCreateResponse createUser(UserDto user) throws Exception;
	
	JSONObject getUser(String token);

	UserEntity loadUserByUsername(String username);
}
