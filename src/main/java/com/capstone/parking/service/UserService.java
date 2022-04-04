package com.capstone.parking.service;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

<<<<<<< Updated upstream
	public List<UserEntity> retriveAllProperties() {
		return userRepository.findAll();
	}
=======
	ResponseEntity changePassword(int userId, String password, String newPassword);

	ResponseEntity updateProfile(int userId, String address, String avatar, String fullName);
>>>>>>> Stashed changes

	ResponseEntity resetPassword(String phoneNumber);

}
