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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.entity.UserEntity;
import com.userservice.model.request.AuthenticationRequest;
import com.userservice.model.request.UserCreateAddressRequest;
import com.userservice.model.request.UserCreateRequest;
import com.userservice.model.request.UserUpdateRequest;
import com.userservice.model.request.WishlistCreateRequest;
import com.userservice.model.response.ErrorMessages;
import com.userservice.model.response.UserCreateAddressResponse;
import com.userservice.model.response.UserCreateAddressResponseDTO;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.model.response.UserUpdateResponse;
import com.userservice.model.response.WishlistProdResponse;
import com.userservice.model.response.WishlistResponse;
import com.userservice.service.UserService;
import com.userservice.service.WishlistService;
import com.userservice.shared.dto.UserDto;
import com.userservice.utils.AuthenticationUtils;
import com.userservice.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = { "http://localhost:3000", "https://www.shofferstop.com" })
public class UserController {

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	UserService userService;
	
	@Autowired
	WishlistService wishlistService;

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
				if (userEntity == null
						|| !authenticationUtils.isValidCredentials(userEntity, authenticationRequest, obj)) {
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
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				return ResponseEntity.ok(userService.getUser(token));
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
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ResponseEntity<?> getAccount(HttpServletRequest request) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				return ResponseEntity.ok(userService.getAccount(token));
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
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAddress(HttpServletRequest request, @RequestParam int page,
			@RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				return ResponseEntity.ok(userService.getUserAddress(token, page, pageSize));
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
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/address/search", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAddress(HttpServletRequest request, @RequestParam String search,
			@RequestParam int page, @RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				return ResponseEntity.ok(userService.getUserSearchAddress(token, search, page, pageSize));
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
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody UserCreateRequest userCreateRequest) throws Exception {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userCreateRequest, userDto);
		try {
			UserCreateResponse createdUser = userService.createUser(userDto);
			return ResponseEntity.ok(createdUser);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/address", method = RequestMethod.POST)
	public ResponseEntity<?> createUserAddress(HttpServletRequest request,
			@RequestBody UserCreateAddressRequest userCreateAddressRequest, @RequestParam int page,
			@RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				UserCreateAddressResponse addresses = userService.createUserAddress(userCreateAddressRequest, token,
						page, pageSize);
				return ResponseEntity.ok(addresses);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/address/default/{addressId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateDefaultAddress(HttpServletRequest request, @PathVariable String addressId,
			@RequestParam int page, @RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				UserCreateAddressResponse addresses = userService.updateDefaultAddress(token, page, pageSize,
						addressId);
				return ResponseEntity.ok(addresses);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.PUT)
	public ResponseEntity<?> createUserAddress(HttpServletRequest request, @PathVariable String addressId,
			@RequestBody UserCreateAddressResponseDTO addressRequest, @RequestParam int page,
			@RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				UserCreateAddressResponse addresses = userService.updateAddress(token, page, pageSize, addressId,
						addressRequest);
				return ResponseEntity.ok(addresses);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddress(HttpServletRequest request, @PathVariable String addressId,
			@RequestParam int page, @RequestParam int pageSize) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				UserCreateAddressResponse addresses = userService.deleteAddress(token, page, pageSize, addressId);
				return ResponseEntity.ok(addresses);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping
	public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody UserUpdateRequest userRequest)
			throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				UserUpdateResponse userResponse = userService.userUpdateRequest(token, userRequest);
				return ResponseEntity.ok(userResponse);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value = "/wishlist", method = RequestMethod.POST)
	public ResponseEntity<?> createUserRating(HttpServletRequest request,@RequestBody WishlistCreateRequest wishlistCreateRequest) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				WishlistResponse totalRatings = wishlistService.createWishlist(token,wishlistCreateRequest);
				return ResponseEntity.ok(totalRatings);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value = "/wishlist", method = RequestMethod.GET)
	public ResponseEntity<?> getWishlist(HttpServletRequest request) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				WishlistResponse totalRatings = wishlistService.getWishlist(token);
				return ResponseEntity.ok(totalRatings);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/wishlist/{productName}", method = RequestMethod.GET)
	public ResponseEntity<?> getWishlistByProductName(HttpServletRequest request,@PathVariable String productName) throws Exception {
		String tokenHeader = request.getHeader("Authorization");
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			token = tokenHeader.substring(7);
			try {
				WishlistProdResponse totalRatings = wishlistService.getWishlistByProdName(token,productName);
				return ResponseEntity.ok(totalRatings);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			return new ResponseEntity<>(obj, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		}
	}
}
