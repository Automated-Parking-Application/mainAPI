package com.capstone.parking.controller;

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
}
