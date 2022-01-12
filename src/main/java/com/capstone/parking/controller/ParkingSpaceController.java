package com.capstone.parking.controller;

import java.util.Map;

import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/parking-space")
public class ParkingSpaceController {
  @Autowired
  private ParkingSpaceService parkingSpaceService;

  @PostMapping("/create")
  public ResponseEntity register(@RequestBody Map<String, String> body, HttpServletRequest request,
      HttpServletResponse response) {
    String name = body.get("name");
    String address = body.get("address");



    return null;
  }

  private int getLoginUserId(HttpServletRequest servletRequest) {
    UserEntity userEntity = (UserEntity) servletRequest.getAttribute("USER_INFO");
    return userEntity.getId();
  }

}
