package com.capstone.parking.controller;

import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.service.UserService;
import com.capstone.parking.utilities.ApaMessage;
import com.capstone.parking.wrapper.SignInBody;
import com.capstone.parking.wrapper.SignUpBody;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity register(@RequestBody SignUpBody signUpBody) {

        if (userService.existsByPhoneNumber(signUpBody.getPhoneNumber())) {
            return new ResponseEntity<>("Phone Number is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        return userService.register(signUpBody);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignInBody body) {
        String phoneNumber = body.getPhoneNumber();
        String password = body.getPassword();
        try {
            ResponseEntity responseEntity = userService.login(phoneNumber, password);
            return responseEntity;
        } catch (Exception ex) {
        }
        return new ResponseEntity<>(new ApaMessage("Cannot login"), HttpStatus.BAD_REQUEST);
    }
}
