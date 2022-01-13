package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingSpaceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Integer> {
  @Override
  <S extends ParkingSpaceEntity> S save(S s);
}
