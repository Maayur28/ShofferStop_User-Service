package com.userservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
	@NonNull
	private String firstName;
	private String lastName;
	private String userDob;
	private String userGender;
}
