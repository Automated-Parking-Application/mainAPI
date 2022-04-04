package com.capstone.parking.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.service.UserService;
import com.capstone.parking.utilities.ApaMessage;
import com.capstone.parking.wrapper.SignInBody;
import com.capstone.parking.wrapper.changePasswordBody;
import com.capstone.parking.wrapper.SignUpBody;
import com.capstone.parking.model.Note;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.service.FirebaseMessagingService;
import com.capstone.parking.service.UserService;
import com.capstone.parking.utilities.ApaMessage;
import com.capstone.parking.wrapper.SignInBody;
import com.capstone.parking.wrapper.SignUpBody;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FirebaseMessagingService firebaseService;
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
        ResponseEntity responseEntity;
        try {
            String phoneNumber = body.getPhoneNumber();
            String password = body.getPassword();
            if (phoneNumber.length() == 0 || password.length() == 0) {
                return new ResponseEntity<>(new ApaMessage("Missing phone number or password"), HttpStatus.BAD_REQUEST);
            }
            responseEntity = userService.login(phoneNumber, password);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApaMessage("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            int userId;
            String password;
            String newPassword;
            try {
                userId = getLoginUserId(request);
                password = (String) body.get("password");
                newPassword = (String) body.get("newPassword");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return userService.changePassword(userId, password, newPassword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        ResponseEntity responseEntity;
        try {
            String phoneNumber = (String) body.get("phoneNumber");
            if (phoneNumber.length() == 0) {
                return new ResponseEntity<>(new ApaMessage("Missing phone number"), HttpStatus.BAD_REQUEST);
            }
            responseEntity = userService.resetPassword(phoneNumber);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApaMessage("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    private int getLoginUserId(HttpServletRequest servletrequest) {
        UserEntity userEntity = (UserEntity) servletrequest.getAttribute("USER_INFO");
        return userEntity.getId();
    @RequestMapping("/send-notification")
    @ResponseBody
    public String sendNotification(@RequestBody Note note) throws FirebaseMessagingException {
        return firebaseService.sendNotificationToATopic(note, "1");
    }
}
