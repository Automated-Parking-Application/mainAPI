package com.capstone.parking.service;

import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.wrapper.SignUpBody;

import org.springframework.http.ResponseEntity;

public interface ParkingSpaceService {

  ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity);

}
