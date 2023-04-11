package com.userservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.userservice.entity.UserAddressEntity;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<UserAddressEntity, String> {

	List<UserAddressEntity> findAllByUserId(String userId);

	@Query("SELECT a FROM userAddress a WHERE a.userId = :userId AND a.defaultAddress = 1")
	UserAddressEntity findAllByUserIdAndDefaultAddress(String userId);

	@Query("SELECT a FROM userAddress a where  a.userId = :userId AND "
			+ "UPPER(a.fullName) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.houseAddress) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.city) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.pincode) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.state) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.country) LIKE concat('%',upper(:search),'%')"
			+ "OR UPPER(a.mobile) LIKE concat('%',upper(:search),'%')")
	Page<UserAddressEntity> findSearchAddressByName(String userId, String search, Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE userAddress SET default_address = 0 where userId = :userId and default_address = 1")
	void updateDefaultAddressToZero(String userId);

	@Modifying
	@Transactional
	@Query("UPDATE userAddress SET default_address = 1 where userId = :userId and addressId = :addressId")
	void updateDefaultAddressToOne(String userId, String addressId);

	@Modifying
	@Transactional
	@Query("UPDATE userAddress SET city = :city,country = :country,fullName = :fullName,houseAddress = :houseAddress,mobile = :mobile,pincode = :pincode,state = :state,defaultAddress = :defaultAddress  where userId = :userId and addressId = :addressId")
	void updateDefaultAddress(String userId, String addressId, String city, String country, String fullName,
			String houseAddress, String mobile, Integer pincode, String state, Integer defaultAddress);

	Page<UserAddressEntity> findPaginatedByUserId(String userId, Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE userAddress SET default_address = 0 where userId = :userId")
	void setAllDefaultAddress(String userId);

	@Modifying
	@Transactional
	void deleteAddressByAddressId(String addressId);
}
