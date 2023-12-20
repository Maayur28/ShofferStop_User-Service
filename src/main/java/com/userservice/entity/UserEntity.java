package com.userservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -5914219496081732356L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 256)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(nullable = false, length = 120, unique = true)
    private String userName;

    @Column(nullable = false, length = 256)
    private String encryptedPassword;

    private String emailVerificationToken;

    @Column(nullable = false)
    private int emailVerificationStatus = 0;

    @Column
    private String userDob;

    @Column
    private String userGender;

}
