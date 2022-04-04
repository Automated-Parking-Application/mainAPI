package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingReservationActivityEntity;
import com.capstone.parking.entity.ParkingReservationActivityKey;
import com.capstone.parking.entity.ParkingReservationEntity;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingReservationActivityRepository
        extends JpaRepository<ParkingReservationActivityEntity, ParkingReservationActivityKey> {
    List<ParkingReservationActivityEntity> findAllByParkingReservationEntity(ParkingReservationEntity parkingRes);

    @Query("SELECT q from ParkingReservationActivityEntity q WHERE q.parkingReservationEntity IN (SELECT r FROM ParkingReservationEntity r WHERE r.parkingId = :parkingId) ORDER BY q.createTime DESC")
    List<ParkingReservationActivityEntity> getHistoryByParkingId(int parkingId);

    @Query("SELECT q from ParkingReservationActivityEntity q WHERE q.parkingReservationEntity IN (SELECT r FROM ParkingReservationEntity r WHERE r.parkingId = :parkingId) AND q.createTime > :startTime AND q.createTime < :endTime AND q.type = :type ORDER BY q.createTime ASC")
    List<ParkingReservationActivityEntity> getParkinngResBetweenTimeByParkingId(int parkingId,
            Timestamp startTime, Timestamp endTime, String type);
}
