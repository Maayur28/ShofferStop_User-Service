package com.userservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.userservice.entity.WishlistEntity;

@Repository
public interface WishlistRepository extends CrudRepository<WishlistEntity, String> {

	@Query("SELECT w FROM wishlists w where w.userId = :userId")
	List<WishlistEntity> getWishlist(String userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM wishlists where userId = :userId and productName = :productName")
	void deleteWishlist(String userId, String productName);
}
