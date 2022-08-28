package com.userservice.service;

import com.userservice.model.request.WishlistCreateRequest;
import com.userservice.model.response.WishlistProdResponse;
import com.userservice.model.response.WishlistResponse;

public interface WishlistService {

	WishlistResponse createWishlist(String token, WishlistCreateRequest wishlistCreateRequest) throws Exception;

	WishlistResponse getWishlist(String token) throws Exception;

	WishlistProdResponse getWishlistByProdName(String token, String productName) throws Exception;
}
