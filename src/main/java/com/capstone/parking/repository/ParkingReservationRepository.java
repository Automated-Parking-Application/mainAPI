package com.capstone.parking.repository;

import java.util.List;

import com.capstone.parking.entity.ParkingReservationEntity;
import com.capstone.parking.entity.QrCodeEntity;
import com.capstone.parking.entity.VehicleEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingReservationRepository extends JpaRepository<ParkingReservationEntity, Integer> {
  List<ParkingReservationEntity> findAllByVehicleEntityAndStatus(VehicleEntity vehicle, String status);

  ParkingReservationEntity getByQrCodeEntityAndStatus(QrCodeEntity qrCodeEntity, String status);

  List<ParkingReservationEntity> findAllByParkingIdAndStatus(int parkingId, String checkOut);
}
