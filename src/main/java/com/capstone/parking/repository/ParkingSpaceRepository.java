package com.capstone.parking.repository;

import com.capstone.parking.entity.ParkingSpaceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, Integer> {
  @Override
  <S extends ParkingSpaceEntity> S save(S s);

  List findAllByOwnerId(int ownerId);

  List findAllByOwnerIdAndStatus(int ownerId, String status);

  @Query("SELECT p from ParkingSpaceEntity p WHERE p.status = 1")
  List<ParkingSpaceEntity> getAllAvailableParkingSpace();
}
