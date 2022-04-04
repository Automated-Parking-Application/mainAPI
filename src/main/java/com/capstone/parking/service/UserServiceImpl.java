package com.capstone.parking.service;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.RoleEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.utilities.ApaMessage;
import com.capstone.parking.wrapper.SignUpBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity register(SignUpBody signUpDto) {
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

    @Override
    public ResponseEntity changePassword(int userId, String password, String newPassword) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
            Map<String, Object> data = new HashMap<>();
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApaMessage("Cannot Update"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity resetPassword(String phoneNumber) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(ApaStatus.USER_DISABLE)) {
                return new ResponseEntity<>(new ApaMessage("Account is disabled"), HttpStatus.NOT_ACCEPTABLE);
            }
            Map<String, Object> data = new HashMap<>();
            data.put(
                    TokenAuthenticationService.HEADER_STRING,
                    TokenAuthenticationService.createToken(userEntity));
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApaMessage("Phone Number or Password is wrong"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity login(String phoneNumber, String password) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            if (userEntity.getStatus().equals(ApaStatus.USER_DISABLE)) {
                return new ResponseEntity<>(new ApaMessage("Account is disabled"), HttpStatus.NOT_ACCEPTABLE);
            }
            Map<String, Object> data = new HashMap<>();
            data.put(
                    TokenAuthenticationService.HEADER_STRING,
                    TokenAuthenticationService.createToken(userEntity));
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApaMessage("Phone Number or Password is wrong"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity updateProfile(int userId, String address, String avatar, String fullName) {
        System.out.println(userId + " hello");
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            userEntity.setAddress(address);
            userEntity.setFullName(fullName);
            userEntity.setAvatar(avatar);
            userRepository.save(userEntity);
            Map<String, Object> data = new HashMap<>();
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApaMessage("Cannot Update"), HttpStatus.BAD_REQUEST);
        }
    }
}
