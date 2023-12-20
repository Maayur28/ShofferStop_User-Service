package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    private String firstName;
    private String lastName;
    private String userDob;
    private String userGender;
}
