package com.capstone.parking.repository;

import com.capstone.parking.entity.QrCodeEntity;
import org.springframework.data.repository.CrudRepository;

public interface QrCodeRepository extends CrudRepository<QrCodeEntity, Integer> {
  @Override
  <S extends QrCodeEntity> S save(S s);

  int countByParkingId(int parkingId);

  int countByParkingIdAndStatus(int parkingId, String status);
}
