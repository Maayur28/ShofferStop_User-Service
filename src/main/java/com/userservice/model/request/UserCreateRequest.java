package com.userservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserCreateRequest {
    @NonNull
    private String firstName;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private boolean remember;
}
