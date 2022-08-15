package com.userservice.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.entity.UserEntity;
import com.userservice.model.request.AuthenticationRequest;
import com.userservice.model.request.UserCreateRequest;
import com.userservice.model.response.ErrorMessages;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.service.UserService;
import com.userservice.shared.dto.UserDto;
import com.userservice.utils.AuthenticationUtils;
import com.userservice.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	UserService userService;

	@Autowired
	AuthenticationUtils authenticationUtils;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		UserEntity userEntity = new UserEntity();
		try {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			if (authenticationUtils.isValidEmail(authenticationRequest.getUserName())) {
				userEntity = userService.loadUserByUsername(authenticationRequest.getUserName());
				if (userEntity == null || !authenticationUtils.isValidCredentials(userEntity, authenticationRequest,obj)) {
					throw new Exception(obj.toString());
				}
			} else {
				throw new Exception(obj.toString());
			}
			Map<String, String> tokens = jwtUtil.generateToken(userEntity, authenticationRequest.isRemember());
			UserCreateResponse userResponse = new UserCreateResponse();
			userResponse.setFirstName(userEntity.getFirstName());
			userResponse.setAccessToken(tokens.get("accessToken"));
			userResponse.setRefreshToken(tokens.get("refreshToken"));
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping
	public ResponseEntity<?> getUser(HttpServletRequest request) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		JSONObject userId = null;
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				userId = userService.getUser(token);
			} catch (IllegalArgumentException e) {
				throw new Exception(e.getMessage());
			} catch (ExpiredJwtException e) {
				throw new Exception(e.getMessage());
			} catch (SignatureException e) {
				throw new Exception(e.getMessage());
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
			}
		} else {
			System.out.println("Bearer String not found in token");
		}
		return ResponseEntity.ok(userId);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody UserCreateRequest userCreateRequest) throws Exception {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userCreateRequest, userDto);
		try {
			UserCreateResponse createdUser = userService.createUser(userDto);
			return ResponseEntity.ok(createdUser);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping
	public String putUser() {
		return "put user called";
	}

	@DeleteMapping
	public String deleteUser() {
		return "delete user called";
	}
}
