package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UserUpdateResponse {
    @NonNull
    private String firstName;
    private String lastName;
    private String userDob;
    private String userGender;
}
