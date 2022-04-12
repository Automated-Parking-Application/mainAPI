package com.capstone.parking.repository;

import com.capstone.parking.entity.QrCodeEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QrCodeRepository extends CrudRepository<QrCodeEntity, Integer> {
  @Override
  <S extends QrCodeEntity> S save(S s);

  int countByParkingId(int parkingId);

  int countByParkingIdAndStatus(int parkingId, String status);

  @Query("SELECT q from QrCodeEntity q WHERE q.parkingId = :parkingId AND q.status = 1 AND q.id NOT IN (SELECT r.codeId FROM ParkingReservationEntity r WHERE r.parkingId = :parkingId AND (r.status = 1 OR r.status = 2))")
  List<QrCodeEntity> getAllAvailableQrCodeFromParkingId(int parkingId);

  QrCodeEntity getByCode(byte[] code);

  Optional<QrCodeEntity> findByExternalId(String externalId);
  Optional<QrCodeEntity> findById(int externalId);

  List<QrCodeEntity> findAllByParkingId(int parkingId);
}
