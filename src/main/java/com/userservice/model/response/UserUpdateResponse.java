package com.userservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateResponse {
	@NonNull
	private String firstName;
	private String lastName;
	private String userDob;
	private String userGender;
}
