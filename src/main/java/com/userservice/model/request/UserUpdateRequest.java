package com.userservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserUpdateRequest {
    @NonNull
    private String firstName;
    private String lastName;
    private String userDob;
    private String userGender;
}
