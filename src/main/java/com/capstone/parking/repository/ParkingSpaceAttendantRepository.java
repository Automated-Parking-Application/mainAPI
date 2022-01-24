package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingSpaceAttendantEntity;
import com.capstone.parking.entity.ParkingSpaceAttendantKey;
import com.capstone.parking.entity.ParkingSpaceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceAttendantRepository extends JpaRepository<ParkingSpaceAttendantEntity, ParkingSpaceAttendantKey> {
  <S extends ParkingSpaceAttendantEntity> S save(S s);

  List<ParkingSpaceAttendantEntity> findAllByParkingSpaceAndStatus(ParkingSpaceEntity parkingSpace, String status);

}
