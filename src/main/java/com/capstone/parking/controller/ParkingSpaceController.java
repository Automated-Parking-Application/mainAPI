package com.capstone.parking.controller;

import java.util.Map;

import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.service.ParkingSpaceService;
import com.capstone.parking.utilities.ApaMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    ParkingSpaceEntity parkingSpaceEntity;
    try {
      parkingSpaceEntity = new ParkingSpaceEntity();
      String name = body.get("name");
      String address = body.get("address");
      int userId = getLoginUserId(request);
      parkingSpaceEntity.setOwnerId(userId);
      parkingSpaceEntity.setName(name);
      parkingSpaceEntity.setAddress(address);
      parkingSpaceEntity.setStatus(ApaStatus.ACTIVE_PARKING_SPACE);
      parkingSpaceEntity = parkingSpaceService.createParkingSpace(parkingSpaceEntity);

    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return new ResponseEntity(parkingSpaceEntity, HttpStatus.OK);
  }

  @DeleteMapping("/{id:[\\d]+}")
  @Transactional
  public ResponseEntity deactivateParkingSpace(@PathVariable("id") int id, HttpServletRequest servletRequest) {
    int userId;
    try {
      userId = getLoginUserId(servletRequest);
    } catch (Exception e) {
      return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.deactivateParkingSpace(id, userId);
  }

  @GetMapping("/")
  public ResponseEntity getAllParkingSpaceByOwnerId(HttpServletRequest request) {
    int ownerId;
    try {
      ownerId = getLoginUserId(request);
    } catch (Exception e) {
      return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.getAllParkingSpaceByOwnerId(ownerId);
  }

  private int getLoginUserId(HttpServletRequest servletRequest) {
    UserEntity userEntity = (UserEntity) servletRequest.getAttribute("USER_INFO");
    return userEntity.getId();
  }

}
