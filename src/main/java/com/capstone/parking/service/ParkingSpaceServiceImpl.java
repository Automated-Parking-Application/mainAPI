package com.capstone.parking.service;

import java.util.List;

import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.repository.ParkingSpaceRepository;
import com.capstone.parking.utilities.ApaMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ParkingSpaceEntity savedParkingSpaceEntity = parkingSpaceRepository.save(parkingSpaceEntity);
    parkingSpaceRepository.flush();
    return savedParkingSpaceEntity;
  }

  @Override
  public ResponseEntity getAllParkingSpaceByOwnerId(int ownerId) {
    List<ParkingSpaceEntity> parkingSpaces;
    try {
      parkingSpaces = parkingSpaceRepository.findAllByOwnerId(ownerId);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(parkingSpaces, HttpStatus.OK);
  }

}
