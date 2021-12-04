package com.capstone.parking.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
	ResponseEntity register(String phoneNumber, String password, String fullname, String address);

}
