package com.capstone.parking.service;

import com.capstone.parking.dto.SignUpDto;

import org.springframework.http.ResponseEntity;

public interface ParkingSpaceService {

  ResponseEntity createParkingSpace(String name, String address);

}
