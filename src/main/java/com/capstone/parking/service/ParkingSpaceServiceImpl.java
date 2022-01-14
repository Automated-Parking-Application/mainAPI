package com.capstone.parking.service;

import java.util.List;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.ParkingSpaceRepository;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.utilities.ApaMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
  ParkingSpaceRepository parkingSpaceRepository;
  UserRepository userRepository;

  @Autowired
  public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, UserRepository userRepository) {
    this.parkingSpaceRepository = parkingSpaceRepository;
    this.userRepository = userRepository;
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

  @Override
  public ResponseEntity deactivateParkingSpace(int id, int userId) {
    ParkingSpaceEntity parkingSpaceEntity = parkingSpaceRepository.getById(id);
    ParkingSpaceEntity deactiveParkingSpaceEntity;
    UserEntity user = userRepository.getById(userId);
    if (parkingSpaceEntity == null) {
      return ResponseEntity.notFound().build();
    }
    if ((parkingSpaceEntity.getOwnerId() == userId || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN))
        && parkingSpaceEntity.getStatus().equals(ApaStatus.ACTIVE_PARKING_SPACE)) {
      try {
        parkingSpaceEntity.setStatus(ApaStatus.DEACTIVE_PARKING_SPACE);
        deactiveParkingSpaceEntity = parkingSpaceRepository.save(parkingSpaceEntity);
      } catch (Exception e) {
        return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return new ResponseEntity("", HttpStatus.OK);
    } else {
      return new ResponseEntity("Cannot access this parking space", HttpStatus.FORBIDDEN);
    }
  }

}
