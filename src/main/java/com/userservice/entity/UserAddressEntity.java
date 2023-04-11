package com.userservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "userAddress")
public class UserAddressEntity implements Serializable {

	private static final long serialVersionUID = -5914219496081732356L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 256,unique = true)
	private String userId;
	
	@Column(nullable = false, length = 256,unique = true)
	private String addressId;

	@Column(nullable = false, length = 256)
	private String country;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private Integer pincode;
	
	@Column(nullable = false)
	private String mobile;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String houseAddress;

	@Column(nullable = false)
	private Integer defaultAddress = 0;
	
}
