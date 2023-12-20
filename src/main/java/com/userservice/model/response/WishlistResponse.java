package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WishlistResponse {
    private List<String> products;
}
