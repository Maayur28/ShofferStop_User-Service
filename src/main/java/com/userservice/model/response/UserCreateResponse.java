package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateResponse {
    private String firstName;
    private String accessToken;
    private String refreshToken;
}
