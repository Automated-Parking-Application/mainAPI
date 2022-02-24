package com.capstone.parking.controller;

import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.service.ParkingSpaceService;
import com.capstone.parking.utilities.ApaMessage;
import java.util.Map;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parking-space")
public class ParkingSpaceController {
  @Autowired
  private ParkingSpaceService parkingSpaceService;

  @PutMapping("/{id:[\\d]+}")
  @Transactional
  public ResponseEntity updateParkingSpace(@RequestBody Map<String, Object> body, @PathVariable("id") int id,
      HttpServletRequest request) {
    ParkingSpaceEntity parkingSpaceEntity = new ParkingSpaceEntity();
    int userId;
    try {
      userId = getLoginUserId(request);
      String name = (String) body.get("name");
      String address = (String) body.get("address");
      String description = (String) body.get("description");
      String image = (String) body.get("image");
      String status = (String) body.get("status");
      Timestamp startTime = new Timestamp((long) body.get("startTime"));
      Timestamp endTime = new Timestamp((long) body.get("endTime"));
      parkingSpaceEntity.setId(id);
      parkingSpaceEntity.setName(name);
      parkingSpaceEntity.setAddress(address);
      parkingSpaceEntity.setStatus(status);
      parkingSpaceEntity.setImage(image);
      parkingSpaceEntity.setDescription(description);
      parkingSpaceEntity.setStartTime(startTime);
      parkingSpaceEntity.setEndTime(endTime);
      System.out.println("name " + name);
    } catch (Exception e) {
      System.out.println("ParkingSpaceController: updateParkingSpace " + e.getMessage());
      return new ResponseEntity<>("cannot update parking space", HttpStatus.CONFLICT);
    }
    return parkingSpaceService.updateParkingSpace(parkingSpaceEntity, userId);

  }

  @PostMapping("/create")
  public ResponseEntity register(@RequestBody Map<String, Object> body, HttpServletRequest request,
      HttpServletResponse response) {
    ParkingSpaceEntity parkingSpaceEntity;
    try {
      parkingSpaceEntity = new ParkingSpaceEntity();
      String name = (String) body.get("name");
      String address = (String) body.get("address");
      String image = (String) body.get("image");
      String description = (String) body.get("description");
      Timestamp startTime = new Timestamp((long) body.get("startTime"));
      Timestamp endTime = new Timestamp((long) body.get("endTime"));
      int userId = getLoginUserId(request);
      parkingSpaceEntity.setOwnerId(userId);
      parkingSpaceEntity.setName(name);
      parkingSpaceEntity.setAddress(address);
      parkingSpaceEntity.setDescription(description);
      parkingSpaceEntity.setImage(image);
      parkingSpaceEntity.setStartTime(startTime);
      parkingSpaceEntity.setEndTime(endTime);
      parkingSpaceEntity.setStatus(ApaStatus.ACTIVE_PARKING_SPACE);
      parkingSpaceEntity = parkingSpaceService.createParkingSpace(parkingSpaceEntity);
    } catch (Exception e) {
      System.out.println("ParkingSpaceController: register " + e.getMessage());
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(parkingSpaceEntity, HttpStatus.OK);
  }

  @DeleteMapping("/{id:[\\d]+}")
  @Transactional
  public ResponseEntity deactivateParkingSpace(@PathVariable("id") int id, HttpServletRequest servletRequest) {
    int userId;
    try {
      userId = getLoginUserId(servletRequest);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.deactivateParkingSpace(id, userId);
  }

  @GetMapping("/")
  public ResponseEntity getAllParkingSpaceByOwnerIdOrByParkingLotAttendantId(HttpServletRequest request) {
    int ownerId;
    try {
      ownerId = getLoginUserId(request);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.getAllParkingSpaceByOwnerIdOrByParkingLotAttendantId(ownerId);
  }

  @PostMapping("/{id:[\\d]+}/user")
  @Transactional
  public ResponseEntity addingParkingSpaceAttendant(@PathVariable("id") int id, @RequestBody Map<String, Object> body,
      HttpServletRequest request) {
    int userId;
    String parkingSpaceAttendantPhoneNumber;
    try {
      parkingSpaceAttendantPhoneNumber = (String) body.get("phoneNumber");
      userId = getLoginUserId(request);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.addingParkingSpaceAttendant(id, userId, parkingSpaceAttendantPhoneNumber);
  }

  @GetMapping("/{id:[\\d]+}/user")
  public ResponseEntity getParkingLotAttendantByParkingId(@PathVariable("id") int id,
      HttpServletRequest request) {
    int userId;
    try {
      userId = getLoginUserId(request);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.getParkingLotAttendantByParkingId(id, userId);
  }

  @DeleteMapping("/{id:[\\d]+}/user/{removedUserId:[\\d]+}")
  public ResponseEntity removeParkingLotAttendant(@PathVariable("id") int id,
      @PathVariable("removedUserId") int removedUserId,
      HttpServletRequest request) {
    int userId;
    try {
      userId = getLoginUserId(request);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
    return parkingSpaceService.removeParkingLotAttendant(id, removedUserId, userId);
  }

  @PostMapping("/{id:[\\d]+}/qr")
  @Transactional
  public ResponseEntity requestQRcodes(@PathVariable("id") int parkingId, @RequestBody Map<String, Object> body,
      HttpServletRequest request) {
    try {
      int userId;
      int numberOfCode;

      try {
        numberOfCode = (int) body.get("number");
        userId = getLoginUserId(request);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return parkingSpaceService.requestQRcodes(parkingId, userId, numberOfCode);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/{id:[\\d]+}/qr/count")
  public ResponseEntity countQRCode(@PathVariable("id") int parkingId,
      HttpServletRequest request) {
    try {
      int userId;
      try {
        userId = getLoginUserId(request);
      } catch (Exception e) {
        return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return parkingSpaceService.countQrCode(parkingId, userId);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
    }
  }

  // Check in API
  @PostMapping("/{id:[\\d]+}/check-in")
  @Transactional
  public ResponseEntity checkIn(@PathVariable("id") int parkingId, @RequestBody Map<String, Object> body,
      HttpServletRequest request) {
    try {
      String plateNumber = (String) body.get("plateNumber");
      String attachment = (String) body.get("attachment");
      String vehicleType = (String) body.get("vehicleType");
      int userId;
      userId = getLoginUserId(request);
      return parkingSpaceService.checkIn(parkingId, userId, vehicleType, plateNumber, attachment);
    } catch (Exception e) {
      System.out.println("ParkingSpaceController: CheckIn: " + e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
  }

  private int getLoginUserId(HttpServletRequest servletRequest) {
    UserEntity userEntity = (UserEntity) servletRequest.getAttribute("USER_INFO");
    return userEntity.getId();
  }

}
