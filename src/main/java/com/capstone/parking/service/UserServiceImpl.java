package com.capstone.parking.service;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.dto.SignUpDto;
import com.capstone.parking.entity.RoleEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.utilities.ApaMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private List<RoleEntity> roleEntities;
    private final RoleEntity userRole;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRole = roleRepository.findTop1ByName(ApaRole.ROLE_ADMIN);
    }

    @Override
    public ResponseEntity register(SignUpDto signUpDto) {
        return register(signUpDto.getPhoneNumber(), signUpDto.getPassword(), signUpDto.getFullName(),
                signUpDto.getAddress(),
                this.userRole);
    }

    public ResponseEntity register(String phoneNumber, String password, String fullname, String address,
            RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();
        try {
            userEntity.setPhoneNumber(phoneNumber);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setFullName(fullname);
            userEntity.setAddress(address);
            userEntity.setStatus(ApaStatus.USER_ENABLE);
            userEntity.setRoleByRoleId(roleEntity);
            userEntity = userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @Override
    public Boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
