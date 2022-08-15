package com.userservice.service.impl;

import static com.userservice.utils.EncryptPassword.createSecretKey;
import static com.userservice.utils.EncryptPassword.encrypt;

import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.userservice.entity.UserEntity;
import com.userservice.model.response.ErrorMessages;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import com.userservice.shared.dto.UserDto;
import com.userservice.utils.JwtTokenUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${user.password.salt}")
	private String salt;

	@Override
	public UserCreateResponse createUser(UserDto user) throws Exception {
		UserEntity userEntity = new UserEntity();
		if (loadUserByUsername(user.getUserName()) != null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}

		BeanUtils.copyProperties(user, userEntity);

		String uniqueID = UUID.randomUUID().toString();
		userEntity.setUserId(uniqueID);
		String password = user.getPassword();
		byte[] saltKey = new String(salt).getBytes();
		SecretKeySpec key = createSecretKey(password.toCharArray(), saltKey, 40000, 128);
		String encryptedPassword = encrypt(password, key);
		userEntity.setEncryptedPassword(encryptedPassword);

		Map<String, String> tokens = jwtTokenUtil.generateToken(userEntity, user.isRemember());
		UserEntity storeUserDetails = userRepository.save(userEntity);
		UserCreateResponse userResponse = new UserCreateResponse();
		userResponse.setFirstName(storeUserDetails.getFirstName());
		userResponse.setAccessToken(tokens.get("accessToken"));
		userResponse.setRefreshToken(tokens.get("refreshToken"));
		return userResponse;
	}

	@Override
	public JSONObject getUser(String token) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		JSONObject obj = new JSONObject();
		obj.put("userId", userId);
		return obj;
	}

	@Override
	public UserEntity loadUserByUsername(String userName) {
		UserEntity userEntity = userRepository.userName(userName);
		return userEntity;
	}

}
