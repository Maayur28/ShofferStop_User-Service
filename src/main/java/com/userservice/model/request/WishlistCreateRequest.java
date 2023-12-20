package com.userservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WishlistCreateRequest {
    private String productName;
}
