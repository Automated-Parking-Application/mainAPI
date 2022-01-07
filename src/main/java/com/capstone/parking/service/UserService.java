package com.capstone.parking.service;

import com.capstone.parking.dto.SignUpDto;

import org.springframework.http.ResponseEntity;

public interface UserService {
	ResponseEntity register(SignUpDto signUpDto);

	Boolean existsByPhoneNumber(String phoneNumber);
	ResponseEntity login(String phoneNumber, String password);

}
