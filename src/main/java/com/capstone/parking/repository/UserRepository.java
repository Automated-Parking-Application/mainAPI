package com.capstone.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.parking.entity.RoleEntity;
import com.capstone.parking.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  @Override
  List<UserEntity> findAll();

  Boolean existsByPhoneNumber(String phoneNumber);

  UserEntity findFirstByPhoneNumber(String phoneNumber);

  UserEntity findFirstByRoleByRoleId(RoleEntity roleEntity);
}
