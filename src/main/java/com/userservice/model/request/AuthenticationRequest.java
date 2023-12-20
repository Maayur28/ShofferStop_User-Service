package com.userservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequest {
    private String userName;
    private String password;
    private boolean remember;
}
