package com.capstone.parking.service;

import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.repository.ParkingSpaceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
  ParkingSpaceRepository parkingSpaceRepository;

  @Autowired
  public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository) {
    this.parkingSpaceRepository = parkingSpaceRepository;
  }

  @Override
  public ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity) {

    parkingSpaceEntity = parkingSpaceRepository.save(parkingSpaceEntity);
    parkingSpaceRepository.flush();
    return parkingSpaceEntity;
  }

}
