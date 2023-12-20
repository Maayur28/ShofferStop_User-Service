package com.userservice.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = -6416119022329580080L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String encryptedPassowrd;
    private String emailVerificationToken;
    private int emailVerificationStatus = 0;
    private boolean remember;
}
