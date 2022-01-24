package com.capstone.parking.service;

import com.capstone.parking.entity.ParkingSpaceEntity;
import org.springframework.http.ResponseEntity;


public interface ParkingSpaceService {

  ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity);

  ResponseEntity getAllParkingSpaceByOwnerId(int ownerId);

  ResponseEntity deactivateParkingSpace(int id, int userId);

  ResponseEntity updateParkingSpace(ParkingSpaceEntity parkingSpaceEntity, int userId);

  ResponseEntity addingParkingSpaceAttendant(int id, int userId, String parkingSpaceAttendantPhoneNumber);

  ResponseEntity getParkingLotAttendantByParkingId(int id, int userId);

}
