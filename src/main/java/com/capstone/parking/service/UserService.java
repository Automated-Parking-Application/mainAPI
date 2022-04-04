package com.capstone.parking.service;

import com.capstone.parking.wrapper.SignUpBody;

import org.springframework.http.ResponseEntity;

public interface UserService {
	ResponseEntity register(SignUpBody signUpDto);

	Boolean existsByPhoneNumber(String phoneNumber);

	ResponseEntity login(String phoneNumber, String password);


	public List<UserEntity> retriveAllProperties() {
		return userRepository.findAll();
	}

	ResponseEntity changePassword(int userId, String password, String newPassword);

	ResponseEntity updateProfile(int userId, String address, String avatar, String fullName);


	ResponseEntity resetPassword(String phoneNumber);

	ResponseEntity updateProfile(int userId, String address, String avatar, String fullName);


}
