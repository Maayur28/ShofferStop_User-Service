package com.userservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateResponse {
    private String firstName;
    private String accessToken;
    private String refreshToken;
}
