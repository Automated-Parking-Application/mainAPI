package com.capstone.parking.service;

import com.capstone.parking.entity.ParkingSpaceEntity;

import org.springframework.http.ResponseEntity;

public interface ParkingSpaceService {

  ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity);

  ResponseEntity getAllParkingSpaceByOwnerId(int ownerId);

}
