package com.capstone.parking.service;

import com.capstone.parking.entity.ParkingSpaceEntity;
import org.springframework.http.ResponseEntity;

public interface ParkingSpaceService {

  ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity);

  ResponseEntity getAllParkingSpaceByOwnerIdOrByParkingLotAttendantId(int ownerId);

  ResponseEntity deactivateParkingSpace(int id, int userId);

  ResponseEntity updateParkingSpace(ParkingSpaceEntity parkingSpaceEntity, int userId);

  ResponseEntity addingParkingSpaceAttendant(int id, int userId, String parkingSpaceAttendantPhoneNumber);

  ResponseEntity getParkingLotAttendantByParkingId(int id, int userId);

  ResponseEntity removeParkingLotAttendant(int id, int removedUserId, int userId);

  ResponseEntity requestQRcodes(int parkingId, int userId, int numberOfCode) throws Exception;

  ResponseEntity countQrCode(int parkingId, int userId);


  ResponseEntity checkIn(int parkingId, int userId, String vehicleType, String plateNumber, String attachment);

}
