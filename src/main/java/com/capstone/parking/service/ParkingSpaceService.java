package com.capstone.parking.service;

import java.util.ArrayList;
import java.util.List;

import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.QrCodeEntity;
import com.capstone.parking.model.ParkingSpaceCronJob;

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

  ResponseEntity getParkingReservationById(int parkingId, int parkingReservationId, int userId);

  ResponseEntity getParkingReservationByCode(int parkingId, String code, int userId);

  QrCodeEntity getQrCodeById(int codeId);

  ResponseEntity getParkingReservationByExternalId(int parkingId, String externalId, int userId);

  ResponseEntity checkOut(int parkingId, int parkingReservationId, int userId);

  ResponseEntity getHistoryByParkingId(int userId, int parkingId);

  List<ParkingSpaceCronJob> getCronJobArray();

  ResponseEntity checkIfAvailableQrCode(int parkingId, String externalId, int userId);


  ResponseEntity checkInWithCode(int parkingId, int userId, int codeId, String vehicleType, String plateNumber,
      String attachment);

}
