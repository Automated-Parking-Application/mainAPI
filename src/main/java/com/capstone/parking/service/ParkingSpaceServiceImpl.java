package com.capstone.parking.service;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.ParkingSpaceAttendantEntity;
import com.capstone.parking.entity.ParkingSpaceAttendantKey;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.RoleEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.model.SMS;
import com.capstone.parking.repository.ParkingSpaceAttendantRepository;
import com.capstone.parking.repository.ParkingSpaceRepository;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.utilities.ApaMessage;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
  private final ParkingSpaceRepository parkingSpaceRepository;
  private final UserRepository userRepository;
  private final ParkingSpaceAttendantRepository parkingSpaceAttendantRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final PasswordSMSService passwordSmsService;

  @Autowired
  public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, UserRepository userRepository,
      ParkingSpaceAttendantRepository parkingSpaceAttendantRepository, RoleRepository roleRepository,
      BCryptPasswordEncoder passwordEncoder, PasswordSMSService passwordSmsService) {
    this.parkingSpaceRepository = parkingSpaceRepository;
    this.userRepository = userRepository;
    this.parkingSpaceAttendantRepository = parkingSpaceAttendantRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.passwordSmsService = passwordSmsService;
  }

  private static String generatePassword(int length) {
    String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    String specialCharacters = "!@#$";
    String numbers = "1234567890";
    String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
    Random random = new Random();
    char[] password = new char[length];

    password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
    password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
    password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
    password[3] = numbers.charAt(random.nextInt(numbers.length()));

    for (int i = 4; i < length; i++) {
      password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
    }
    return String.valueOf(password);
  }

  public UserEntity generateParkingSpaceAttendant(String phoneNumber) {
    UserEntity userEntity = new UserEntity();
    try {
      String newPassword = generatePassword(8);
      String newEncodePassword = passwordEncoder.encode(newPassword);
      RoleEntity roleEntity = roleRepository.findTop1ByName(ApaRole.ROLE_PARKING_ATTENDANTS);
      userEntity.setPhoneNumber(phoneNumber);
      userEntity.setPassword(newEncodePassword);
      userEntity.setStatus(ApaStatus.USER_ENABLE);
      userEntity.setRoleByRoleId(roleEntity);
      sendPasswordToPhoneNumber(phoneNumber, newPassword);
      userEntity = userRepository.save(userEntity);
    } catch (Exception e) {
      return null;
    }
    System.out.println(userRepository.findFirstByPhoneNumber(phoneNumber).getId());

    return userRepository.findFirstByPhoneNumber(phoneNumber);
  }

  @Override
  public ParkingSpaceEntity createParkingSpace(ParkingSpaceEntity parkingSpaceEntity) {
    ParkingSpaceEntity savedParkingSpaceEntity = parkingSpaceRepository.save(parkingSpaceEntity);
    parkingSpaceRepository.flush();
    return savedParkingSpaceEntity;
  }

  @Override
  public ResponseEntity getAllParkingSpaceByOwnerId(int ownerId) {
    List<ParkingSpaceEntity> parkingSpaces;
    try {
      parkingSpaces = parkingSpaceRepository.findAllByOwnerId(ownerId);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(parkingSpaces, HttpStatus.OK);
  }

  @Override
  public ResponseEntity deactivateParkingSpace(int id, int userId) {
    ParkingSpaceEntity parkingSpaceEntity = parkingSpaceRepository.getById(id);
    ParkingSpaceEntity deactiveParkingSpaceEntity;
    UserEntity user = userRepository.getById(userId);
    if (parkingSpaceEntity == null) {
      return ResponseEntity.notFound().build();
    }
    if ((parkingSpaceEntity.getOwnerId() == userId || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN))
        && parkingSpaceEntity.getStatus().equals(ApaStatus.ACTIVE_PARKING_SPACE)) {
      try {
        parkingSpaceEntity.setStatus(ApaStatus.DEACTIVE_PARKING_SPACE);
        deactiveParkingSpaceEntity = parkingSpaceRepository.save(parkingSpaceEntity);
      } catch (Exception e) {
        return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return new ResponseEntity("", HttpStatus.OK);
    } else {
      return new ResponseEntity("Cannot access this parking space", HttpStatus.FORBIDDEN);
    }
  }

  @Override
  public ResponseEntity updateParkingSpace(ParkingSpaceEntity parkingSpaceEntity, int userId) {
    ParkingSpaceEntity updatedParkingSpaceEntity = parkingSpaceRepository.getById(parkingSpaceEntity.getId());
    UserEntity user = userRepository.getById(userId);
    if (updatedParkingSpaceEntity == null) {
      return ResponseEntity.notFound().build();
    }
    if (updatedParkingSpaceEntity.getOwnerId() == user.getId()
        || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN)) {
      try {
        parkingSpaceRepository.save(parkingSpaceEntity);
      } catch (Exception e) {
        System.out.println("ParkingSpaceService: updateParkingSpace " + e.getMessage());
        return new ResponseEntity(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return new ResponseEntity("", HttpStatus.OK);

    } else {
      return new ResponseEntity("Cannot access this parking space", HttpStatus.FORBIDDEN);
    }
  }

  private boolean checkIfHavingAdminPermission(int parkingId, int userId) {
    try {
      ParkingSpaceEntity searchParkingSpace = parkingSpaceRepository.getById(parkingId);
      UserEntity user = userRepository.getById(userId);
      return searchParkingSpace != null || searchParkingSpace.getOwnerId() == user.getId()
          || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean sendPasswordToPhoneNumber(String phoneNumber, String password) {
    SMS sms = new SMS("+84" + phoneNumber.substring(1, phoneNumber.length()), password);
    return passwordSmsService.send(sms);
  }

  @Override
  public ResponseEntity addingParkingSpaceAttendant(int parkingId, int userId,
      String parkingSpaceAttendantPhoneNumber) {
    // TODO Check if parking attendant already in parking space
    // TODO check if send password successfully or not
    if (checkIfHavingAdminPermission(parkingId, userId)) {
      UserEntity foundUser = userRepository.findFirstByPhoneNumber(parkingSpaceAttendantPhoneNumber);
      if (foundUser == null) {
        UserEntity createdParkingSpaceAttendant = generateParkingSpaceAttendant(parkingSpaceAttendantPhoneNumber);
        if (createdParkingSpaceAttendant != null) {
          ParkingSpaceAttendantEntity newParkingSpaceAttendant = new ParkingSpaceAttendantEntity(
              new ParkingSpaceAttendantKey(createdParkingSpaceAttendant.getId(), parkingId));
          parkingSpaceAttendantRepository.save(newParkingSpaceAttendant);
          return new ResponseEntity("Ok", HttpStatus.OK);
        } else {
          return new ResponseEntity("Cannot create account from this phone number", HttpStatus.BAD_REQUEST);
        }
      } else {
        if (foundUser.getRoleByRoleId().getName().equals(ApaRole.ROLE_PARKING_ATTENDANTS)) {
          try {
            ParkingSpaceAttendantEntity newParkingSpaceAttendant = new ParkingSpaceAttendantEntity(
                new ParkingSpaceAttendantKey(foundUser.getId(), parkingId));
            parkingSpaceAttendantRepository.save(newParkingSpaceAttendant);
            return new ResponseEntity("", HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
        } else {
          return new ResponseEntity("Cannot add this phone number to parking space", HttpStatus.BAD_REQUEST);
        }
      }
    } else {
      return new ResponseEntity("Cannot access this parking space", HttpStatus.FORBIDDEN);
    }
  }

  @Override
  public ResponseEntity getParkingLotAttendantByParkingId(int parkingId, int userId) {
    try {
      if (checkIfHavingAdminPermission(parkingId, userId)) {
        ParkingSpaceEntity parkingSpace = parkingSpaceRepository.getById(parkingId);
        List<ParkingSpaceAttendantEntity> parkingLotAttendants = parkingSpaceAttendantRepository
            .findAllByParkingSpaceAndStatus(parkingSpace, ApaStatus.ACTIVE_PARKING_SPACE_ATTENDANT).stream()
            .filter(p -> p.getUser().getStatus().equals(ApaStatus.USER_ENABLE)).collect(Collectors.toList());

        return new ResponseEntity(parkingLotAttendants, HttpStatus.OK);
      } else
        return new ResponseEntity("Cannot access this parking space", HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
