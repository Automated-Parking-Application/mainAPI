package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingReservationActivityEntity;
import com.capstone.parking.entity.ParkingReservationActivityKey;
import com.capstone.parking.entity.ParkingReservationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingReservationActivityRepository
    extends JpaRepository<ParkingReservationActivityEntity, ParkingReservationActivityKey> {
        List<ParkingReservationActivityEntity> findAllByParkingReservationEntity(ParkingReservationEntity parkingRes);
}
