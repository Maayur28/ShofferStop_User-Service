package com.userservice.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.userservice.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
	UserEntity userName(String userName);

	UserEntity userId(String userId);

	@Modifying
	@Transactional
	@Query("UPDATE users SET firstName = :firstName,lastName = :lastName,userDob = :userDob,userGender = :userGender where userId = :userId")
	void updateprofile(String userId, String firstName, String lastName, String userDob, String userGender);
}
