package com.userservice.service.impl;

import static com.userservice.utils.EncryptPassword.createSecretKey;
import static com.userservice.utils.EncryptPassword.encrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.userservice.entity.UserAddressEntity;
import com.userservice.entity.UserEntity;
import com.userservice.model.request.UserCreateAddressRequest;
import com.userservice.model.request.UserUpdateRequest;
import com.userservice.model.response.Account;
import com.userservice.model.response.ErrorMessages;
import com.userservice.model.response.Pagination;
import com.userservice.model.response.UserCreateAddressResponse;
import com.userservice.model.response.UserCreateAddressResponseDTO;
import com.userservice.model.response.UserCreateResponse;
import com.userservice.model.response.UserIdResponse;
import com.userservice.model.response.UserUpdateResponse;
import com.userservice.repository.AddressRepository;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import com.userservice.shared.dto.UserDto;
import com.userservice.utils.JwtTokenUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${user.password.salt}")
	private String salt;

	@Override
	public UserCreateResponse createUser(UserDto user) throws Exception {
		UserEntity userEntity = new UserEntity();
		if (loadUserByUsername(user.getUserName()) != null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage());
			throw new Exception(obj.toString());
		}

		BeanUtils.copyProperties(user, userEntity);

		String uniqueID = UUID.randomUUID().toString();
		userEntity.setUserId(uniqueID);
		String password = user.getPassword();
		byte[] saltKey = salt.getBytes();
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
	public UserCreateAddressResponse createUserAddress(UserCreateAddressRequest userAddress, String token, int page,
			int pageSize) throws Exception {
		UserAddressEntity userEntity = new UserAddressEntity();
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
			throw new Exception(obj.toString());
		}

		BeanUtils.copyProperties(userAddress, userEntity);
		String uniqueID = UUID.randomUUID().toString();

		userEntity.setUserId(userId);
		userEntity.setAddressId(uniqueID);
		if (!userAddress.isDefaultAddress()) {
			List<UserAddressEntity> addressList = addressRepository.findAllByUserId(userId);
			if (addressList == null || addressList.isEmpty()) {
				userEntity.setDefaultAddress(1);
			} else {
				userEntity.setDefaultAddress(0);
			}
		} else {
			userEntity.setDefaultAddress(1);
		}
		if (userEntity.getDefaultAddress() == 1) {
			addressRepository.setAllDefaultAddress(userId);
		}
		addressRepository.save(userEntity);
		Pageable pageable = PageRequest.of(page - 1, pageSize,
				Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
		Page<UserAddressEntity> addressList = addressRepository.findPaginatedByUserId(userId, pageable);
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
		for (UserAddressEntity addEntity : addressList) {
			UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
			BeanUtils.copyProperties(addEntity, address);
			addresses.add(address);
		}
		Pagination pagination = new Pagination();
		pagination.setPage(addressList.getPageable().getPageNumber() + 1);
		pagination.setPageSize(addressList.getPageable().getPageSize());

		addressResponse.setTotal(addressList.getTotalElements());
		addressResponse.setPagination(pagination);
		addressResponse.setAddresses(addresses);
		return addressResponse;
	}

	@Override
	public UserIdResponse getUser(String token) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserIdResponse userIdResponse = new UserIdResponse();
		userIdResponse.setUserId(userId);
		
		return userIdResponse;
	}

	@Override
	public UserCreateAddressResponse getUserAddress(String token, int page, int pageSize) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		if (userId != null && page != 0 && pageSize != 0) {

			Pageable pageable = PageRequest.of(page - 1, pageSize,
					Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
			Page<UserAddressEntity> addressList = addressRepository.findPaginatedByUserId(userId, pageable);

			List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
			for (UserAddressEntity addEntity : addressList.getContent()) {
				UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
				BeanUtils.copyProperties(addEntity, address);
				addresses.add(address);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(addressList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(addressList.getPageable().getPageSize());

			addressResponse.setTotal(addressList.getTotalElements());
			addressResponse.setPagination(pagination);
			addressResponse.setAddresses(addresses);
		}
		return addressResponse;
	}
	
	@Override
	public UserCreateAddressResponse getUserSearchAddress(String token,String search, int page, int pageSize) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		
		if (userId != null && page != 0 && pageSize != 0) {
			Pageable pageable = PageRequest.of(page - 1, pageSize,
					Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
			Page<UserAddressEntity> addressList = addressRepository.findSearchAddressByName(userId,search,pageable);

			List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
			for (UserAddressEntity addEntity : addressList.getContent()) {
				UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
				BeanUtils.copyProperties(addEntity, address);
				addresses.add(address);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(addressList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(addressList.getPageable().getPageSize());

			addressResponse.setTotal(addressList.getTotalElements());
			addressResponse.setPagination(pagination);
			addressResponse.setAddresses(addresses);
		}
		return addressResponse;
	}

	@Override
	public UserCreateAddressResponse updateDefaultAddress(String token, int page, int pageSize, String addressId) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		if (userId != null && page != 0 && pageSize != 0) {

			addressRepository.updateDefaultAddressToZero(userId);
			addressRepository.updateDefaultAddressToOne(userId, addressId);

			Pageable pageable = PageRequest.of(0, pageSize,
					Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
			Page<UserAddressEntity> addressList = addressRepository.findPaginatedByUserId(userId, pageable);
			List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
			for (UserAddressEntity addEntity : addressList.getContent()) {
				UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
				BeanUtils.copyProperties(addEntity, address);
				addresses.add(address);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(addressList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(addressList.getPageable().getPageSize());

			addressResponse.setTotal(addressList.getTotalElements());
			addressResponse.setPagination(pagination);
			addressResponse.setAddresses(addresses);
		}
		return addressResponse;
	}

	@Override
	public UserCreateAddressResponse updateAddress(String token, int page, int pageSize, String addressId,
			UserCreateAddressResponseDTO addressRequest) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		if (userId != null && page != 0 && pageSize != 0) {

			if (addressRequest.getDefaultAddress() == 1) {
				addressRepository.updateDefaultAddressToZero(userId);
			}
			addressRepository.updateDefaultAddress(userId, addressId, addressRequest.getCity(),
					addressRequest.getCountry(), addressRequest.getFullName(), addressRequest.getHouseAddress(),
					addressRequest.getMobile(), addressRequest.getPincode(), addressRequest.getState(),
					addressRequest.getDefaultAddress());

			Pageable pageable = PageRequest.of(0, pageSize,
					Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
			Page<UserAddressEntity> addressList = addressRepository.findPaginatedByUserId(userId, pageable);
			List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
			for (UserAddressEntity addEntity : addressList.getContent()) {
				UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
				BeanUtils.copyProperties(addEntity, address);
				addresses.add(address);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(addressList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(addressList.getPageable().getPageSize());

			addressResponse.setTotal(addressList.getTotalElements());
			addressResponse.setPagination(pagination);
			addressResponse.setAddresses(addresses);
		}
		return addressResponse;
	}

	@Override
	public UserCreateAddressResponse deleteAddress(String token, int page, int pageSize, String addressId) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserCreateAddressResponse addressResponse = new UserCreateAddressResponse();
		if (userId != null && page != 0 && pageSize != 0) {

			addressRepository.deleteAddressByAddressId(addressId);

			Pageable pageable = PageRequest.of(page - 1, pageSize,
					Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
			Page<UserAddressEntity> addressList = addressRepository.findPaginatedByUserId(userId, pageable);
			if (page >= 2 && (addressList == null || addressList.isEmpty())) {
				pageable = PageRequest.of(page - 2, pageSize,
						Sort.by("defaultAddress").descending().and(Sort.by("id").descending()));
				addressList = addressRepository.findPaginatedByUserId(userId, pageable);
			}
			List<UserCreateAddressResponseDTO> addresses = new ArrayList<>();
			for (UserAddressEntity addEntity : addressList.getContent()) {
				UserCreateAddressResponseDTO address = new UserCreateAddressResponseDTO();
				BeanUtils.copyProperties(addEntity, address);
				addresses.add(address);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(addressList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(addressList.getPageable().getPageSize());

			addressResponse.setTotal(addressList.getTotalElements());
			addressResponse.setPagination(pagination);
			addressResponse.setAddresses(addresses);
		}
		return addressResponse;
	}

	@Override
	public Account getAccount(String token) {
		String userId = null;
		Account account = new Account();
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		if (userId != null) {
			UserEntity userEntity = userRepository.userId(userId);
			BeanUtils.copyProperties(userEntity, account);
		}
		return account;
	}

	@Override
	public UserUpdateResponse userUpdateRequest(String token, UserUpdateRequest userUpdateRequest) {
		String userId = null;
		if (!jwtTokenUtil.validateToken(token)) {
			userId = jwtTokenUtil.getUserIdFromToken(token);
		}
		UserUpdateResponse userResponse = new UserUpdateResponse();
		if (userId != null) {
			userRepository.updateprofile(userId, userUpdateRequest.getFirstName(), userUpdateRequest.getLastName(),
					userUpdateRequest.getUserDob(), userUpdateRequest.getUserGender());
			UserEntity userEntity = userRepository.userId(userId);
			BeanUtils.copyProperties(userEntity, userResponse);
		}
		return userResponse;
	}

	@Override
	public UserEntity loadUserByUsername(String userName) {
		UserEntity userEntity = userRepository.userName(userName);
		return userEntity;
	}	

}
