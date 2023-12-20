package com.userservice.service.impl;

import com.userservice.entity.WishlistEntity;
import com.userservice.model.request.WishlistCreateRequest;
import com.userservice.model.response.ErrorMessages;
import com.userservice.model.response.WishlistProdResponse;
import com.userservice.model.response.WishlistResponse;
import com.userservice.repository.WishlistRepository;
import com.userservice.service.WishlistService;
import com.userservice.utils.JwtTokenUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public WishlistResponse createWishlist(String token, WishlistCreateRequest wishlistCreateRequest) throws Exception {
        String userId = null;
        if (!jwtTokenUtil.validateToken(token)) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }
        if (userId == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
            throw new Exception(obj.toString());
        }

        List<WishlistEntity> userWishlist = wishlistRepository.getWishlist(userId);

        boolean productFound = false;
        if (userWishlist != null) {
            for (WishlistEntity wishlist : userWishlist) {
                if (wishlist.getProductName().equals(wishlistCreateRequest.getProductName())) {
                    productFound = true;
                    break;
                }
            }
        }

        saveOrUpdateWishlist(userWishlist, productFound, userId, wishlistCreateRequest);

        userWishlist = wishlistRepository.getWishlist(userId);
        List<String> wishlists = new ArrayList<>();
        if (userWishlist != null) {
            for (WishlistEntity wishlist : userWishlist) {
                wishlists.add(wishlist.getProductName());
            }
        }
        WishlistResponse wishlistResponse = new WishlistResponse();
        wishlistResponse.setProducts(wishlists);

        return wishlistResponse;
    }

    @Transactional
    private void saveOrUpdateWishlist(List<WishlistEntity> userWishlist, boolean productFound,
                                      String userId, WishlistCreateRequest wishlistCreateRequest) {
        if (userWishlist == null || !productFound) {
            WishlistEntity wishlistRequest = new WishlistEntity();
            wishlistRequest.setUserId(userId);
            wishlistRequest.setProductName(wishlistCreateRequest.getProductName());
            wishlistRepository.save(wishlistRequest);
        } else {
            wishlistRepository.deleteWishlist(userId, wishlistCreateRequest.getProductName());
        }
    }

    @Override
    public WishlistResponse getWishlist(String token) throws Exception {
        String userId = null;
        if (!jwtTokenUtil.validateToken(token)) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }
        if (userId == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
            throw new Exception(obj.toString());
        }

        List<WishlistEntity> userWishlist = wishlistRepository.getWishlist(userId);

        List<String> wishlists = new ArrayList<>();
        if (userWishlist != null) {
            for (WishlistEntity wishlist : userWishlist) {
                wishlists.add(wishlist.getProductName());
            }
        }
        WishlistResponse wishlistResponse = new WishlistResponse();
        wishlistResponse.setProducts(wishlists);

        return wishlistResponse;
    }

    @Override
    public WishlistResponse deleteWishlist(String token, String wishlistId) throws Exception {
        String userId = null;
        if (!jwtTokenUtil.validateToken(token)) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }
        if (userId == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
            throw new Exception(obj.toString());
        }

        wishlistRepository.deleteWishlist(userId, wishlistId);

        List<WishlistEntity> userWishlist = wishlistRepository.getWishlist(userId);

        List<String> wishlists = new ArrayList<>();
        if (userWishlist != null) {
            for (WishlistEntity wishlist : userWishlist) {
                wishlists.add(wishlist.getProductName());
            }
        }
        WishlistResponse wishlistResponse = new WishlistResponse();
        wishlistResponse.setProducts(wishlists);

        return wishlistResponse;
    }

    @Override
    public WishlistProdResponse getWishlistByProdName(String token, String productName) throws Exception {
        String userId = null;
        if (!jwtTokenUtil.validateToken(token)) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }
        if (userId == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
            throw new Exception(obj.toString());
        }

        List<WishlistEntity> userWishlist = wishlistRepository.getWishlist(userId);

        boolean wishlisted = false;
        if (userWishlist != null) {
            for (WishlistEntity wishlist : userWishlist) {
                if (wishlist.getProductName().equals(productName)) {
                    wishlisted = true;
                    break;
                }
            }
        }
        WishlistProdResponse wishlistProdResponse = new WishlistProdResponse();
        wishlistProdResponse.setWishlisted(wishlisted);

        return wishlistProdResponse;
    }

}
