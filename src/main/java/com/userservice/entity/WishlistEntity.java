package com.userservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity(name = "wishlists")
public class WishlistEntity implements Serializable {

    private static final long serialVersionUID = -4790658905115028616L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 512)
    private String productName;

    @Column(nullable = false, length = 64)
    private String userId;

}
