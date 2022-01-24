package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingSpaceAttendantEntity;
import com.capstone.parking.entity.ParkingSpaceAttendantKey;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParkingSpaceAttendantRepository extends JpaRepository<ParkingSpaceAttendantEntity, ParkingSpaceAttendantKey> {
  <S extends ParkingSpaceAttendantEntity> S save(S s);

}
