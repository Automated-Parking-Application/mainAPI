package com.capstone.parking.repository;

import com.capstone.parking.entity.RoleEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    @Override
    List<RoleEntity> findAll();

    RoleEntity findFirstById(int id);

    RoleEntity findTop1ByName(String name);
}
