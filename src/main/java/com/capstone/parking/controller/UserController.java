package com.capstone.parking.controller;


import com.capstone.parking.service.UserService;
import com.capstone.parking.utilities.ApaMessage;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
    public ResponseEntity register(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String password = body.get("password");
        String fullName = body.get("fullName");
        String address = body.get("address");
        if (fullName == null || fullName.length() == 0) {
            return new ResponseEntity(new ApaMessage("Fullname is null"), HttpStatus.CONFLICT);
        };
        return userService.register(phoneNumber, password, fullName, address);
    }
}
