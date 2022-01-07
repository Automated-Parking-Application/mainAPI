package com.capstone.parking.controller;

import com.capstone.parking.dto.SignUpDto;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.service.UserService;
import com.capstone.parking.utilities.ApaMessage;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.capstone.parking.dto.SignUpDto;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignUpDto signUpDto) {

        if (userService.existsByPhoneNumber(signUpDto.getPhoneNumber())) {
            return new ResponseEntity<>("Phone Number is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        return userService.register(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String password = body.get("password");
        try {
            ResponseEntity responseEntity = userService.login(phoneNumber, password);
            return responseEntity;
        } catch (Exception ex) {
        }
        return new ResponseEntity<>(new ApaMessage("Cannot login"), HttpStatus.BAD_REQUEST);
    }
}
