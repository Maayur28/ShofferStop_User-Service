package com.userservice.model.response;

public enum ErrorMessages {

	AUTHENTICATION_FAILED("Invalid username or password"),
	USER_ALREADY_EXISTS("User already exists");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
