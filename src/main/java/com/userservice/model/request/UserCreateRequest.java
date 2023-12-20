package com.userservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
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
