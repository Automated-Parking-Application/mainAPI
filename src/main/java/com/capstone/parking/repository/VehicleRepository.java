package com.capstone.parking.repository;

import com.capstone.parking.entity.VehicleEntity;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<VehicleEntity, Integer> {
  VehicleEntity findTop1ByPlateNumber(String plateNumber);
}
