package com.capstone.parking.service;

import com.capstone.parking.wrapper.SignUpBody;

import org.springframework.http.ResponseEntity;

public interface UserService {
	ResponseEntity register(SignUpBody signUpDto);

	Boolean existsByPhoneNumber(String phoneNumber);

	ResponseEntity login(String phoneNumber, String password);

	ResponseEntity updateProfile(int userId, String address, String avatar, String fullName);

}
