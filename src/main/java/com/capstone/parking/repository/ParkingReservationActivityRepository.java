package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingReservationActivityEntity;
import com.capstone.parking.entity.ParkingReservationActivityKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingReservationActivityRepository
    extends JpaRepository<ParkingReservationActivityEntity, ParkingReservationActivityKey> {
}
