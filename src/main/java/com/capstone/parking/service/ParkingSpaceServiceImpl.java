package com.capstone.parking.service;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.constants.ApaStatus;
import com.capstone.parking.entity.ParkingReservationActivityEntity;
import com.capstone.parking.entity.ParkingReservationActivityKey;
import com.capstone.parking.entity.ParkingReservationEntity;
import com.capstone.parking.entity.ParkingSpaceAttendantEntity;
import com.capstone.parking.entity.ParkingSpaceAttendantKey;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.QrCodeEntity;
import com.capstone.parking.entity.RoleEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.entity.VehicleEntity;
import com.capstone.parking.model.ParkingSpaceCronJob;
import com.capstone.parking.model.SMS;
import com.capstone.parking.repository.ParkingReservationActivityRepository;
import com.capstone.parking.repository.ParkingReservationRepository;
import com.capstone.parking.repository.ParkingSpaceAttendantRepository;
import com.capstone.parking.repository.ParkingSpaceRepository;
import com.capstone.parking.repository.QrCodeRepository;
import com.capstone.parking.repository.RoleRepository;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.repository.VehicleRepository;
import com.capstone.parking.utilities.ApaMessage;
import com.capstone.parking.wrapper.ParkingReservationResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONObject;
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
  private final QRCodeService qrCodeService;
  private final QrCodeRepository qrCodeRepository;
  private final VehicleRepository vehicleRepository;
  private final ParkingReservationRepository parkingReservationRepository;
  private final ParkingReservationActivityRepository parkingReservationActivityRepository;

  @Autowired
  public ParkingSpaceServiceImpl(ParkingSpaceRepository parkingSpaceRepository, UserRepository userRepository,
      ParkingSpaceAttendantRepository parkingSpaceAttendantRepository, RoleRepository roleRepository,
      BCryptPasswordEncoder passwordEncoder, PasswordSMSService passwordSmsService, QRCodeService qrCodeService,
      QrCodeRepository qrCodeRepository, VehicleRepository vehicleRepository,
      ParkingReservationRepository parkingReservationRepository,
      ParkingReservationActivityRepository parkingReservationActivityRepository) {
    this.parkingSpaceRepository = parkingSpaceRepository;
    this.userRepository = userRepository;
    this.parkingSpaceAttendantRepository = parkingSpaceAttendantRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.passwordSmsService = passwordSmsService;
    this.qrCodeService = qrCodeService;
    this.qrCodeRepository = qrCodeRepository;
    this.vehicleRepository = vehicleRepository;
    this.parkingReservationRepository = parkingReservationRepository;
    this.parkingReservationActivityRepository = parkingReservationActivityRepository;
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
  public ResponseEntity getAllParkingSpaceByOwnerIdOrByParkingLotAttendantId(int ownerId) {
    List<ParkingSpaceEntity> parkingSpaces;
    try {
      UserEntity user = userRepository.getById(ownerId);
      if (user.getRoleByRoleId().getName().equals(ApaRole.ROLE_ADMIN)) {
        parkingSpaces = parkingSpaceRepository.findAllByOwnerIdAndStatus(ownerId,
            (String) ApaStatus.ACTIVE_PARKING_SPACE);
      } else if (user.getRoleByRoleId().getName().equals(ApaRole.ROLE_PARKING_ATTENDANTS)) {
        List<ParkingSpaceAttendantEntity> parkingSpacesAttendants = parkingSpaceAttendantRepository
            .findAllByUserAndStatus(user,
                (String) ApaStatus.ACTIVE_PARKING_SPACE);
        parkingSpaces = parkingSpacesAttendants.stream().map(ParkingSpaceAttendantEntity::getParkingSpace)
            .collect(Collectors.toList());
      } else {
        return new ResponseEntity<>(new ApaMessage("Something went wrong"), HttpStatus.BAD_REQUEST);
      }
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
        return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return new ResponseEntity<>("", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.FORBIDDEN);
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
        return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.CONFLICT);
      }
      return new ResponseEntity<>(parkingSpaceEntity, HttpStatus.OK);

    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.FORBIDDEN);
    }
  }

  private boolean checkIfHavingAdminPermission(int parkingId, int userId) {
    try {
      ParkingSpaceEntity searchParkingSpace = parkingSpaceRepository.getById(parkingId);
      UserEntity user = userRepository.getById(userId);
      return (searchParkingSpace != null && searchParkingSpace.getOwnerId() == user.getId())
          || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean checkIfHavingParkingLotAttendantPermission(int parkingId, int userId) {
    try {
      List<ParkingSpaceAttendantEntity> foundParkingSpaceAttendant = parkingSpaceAttendantRepository
          .findAllByIdAndStatus(
              new ParkingSpaceAttendantKey(userId, parkingId), ApaStatus.ACTIVE_PARKING_SPACE_ATTENDANT);
      return !foundParkingSpaceAttendant.isEmpty();
    } catch (Exception e) {
      return false;
    }
  }

  private boolean checkExistedParkedVehicle(String plateNumber) {
    try {
      VehicleEntity vehicle = vehicleRepository.findTop1ByPlateNumber(plateNumber);
      List<ParkingReservationEntity> existedReservation = parkingReservationRepository
          .findAllByVehicleEntityAndStatus(vehicle, ApaStatus.CHECK_IN);
      List<ParkingReservationEntity> existedArchivedReservation = parkingReservationRepository
          .findAllByVehicleEntityAndStatus(vehicle, ApaStatus.ARCHIVED);
      return !existedReservation.isEmpty() || !existedArchivedReservation.isEmpty();
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
          ParkingSpaceAttendantEntity res = parkingSpaceAttendantRepository.save(newParkingSpaceAttendant);
          return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Cannot create account from this phone number", HttpStatus.BAD_REQUEST);
        }
      } else {
        if (foundUser.getRoleByRoleId().getName().equals(ApaRole.ROLE_PARKING_ATTENDANTS)) {
          try {
            ParkingSpaceAttendantEntity newParkingSpaceAttendant = new ParkingSpaceAttendantEntity(
                new ParkingSpaceAttendantKey(foundUser.getId(), parkingId));
            ParkingSpaceAttendantEntity res = parkingSpaceAttendantRepository.save(newParkingSpaceAttendant);
            return new ResponseEntity<>(res, HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
        } else {
          return new ResponseEntity<>("Cannot add this phone number to parking space", HttpStatus.BAD_REQUEST);
        }
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.FORBIDDEN);
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

        return new ResponseEntity<>(parkingLotAttendants, HttpStatus.OK);
      } else
        return new ResponseEntity<>("Cannot access this parking space", HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity removeParkingLotAttendant(int parkingId, int removedUserId, int userId) {
    try {
      if (checkIfHavingAdminPermission(parkingId, userId)) {
        ParkingSpaceAttendantEntity disabledParkingSpaceAttendant = parkingSpaceAttendantRepository
            .getById(new ParkingSpaceAttendantKey(removedUserId, parkingId));

        disabledParkingSpaceAttendant.setStatus(ApaStatus.DEACTIVE_PARKING_SPACE_ATTENDANT);
        parkingSpaceAttendantRepository.save(disabledParkingSpaceAttendant);

        return new ResponseEntity<>("", HttpStatus.OK);
      } else
        return new ResponseEntity<>("Cannot access this parking space", HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity requestQRcodes(int parkingId, int userId, int numberOfCode) throws Exception {
    if (checkIfHavingAdminPermission(parkingId, userId)) {
      IntStream.range(0, numberOfCode).forEach(i -> {
        try {
          long createdAt = System.currentTimeMillis();
          JSONObject obj = new JSONObject();
          String newCrypt = generatePassword(8);
          obj.put("parking", parkingId);
          obj.put("createdAt", createdAt);
          obj.put("parkingReservation", newCrypt);
          byte[] code = qrCodeService.generateQRCode(obj.toString(), 500, 500);
          QrCodeEntity qrCodeEntity = new QrCodeEntity();
          qrCodeEntity.setCode(code);
          qrCodeEntity.setExternalId(newCrypt);
          qrCodeEntity.setParkingId(parkingId);
          qrCodeEntity.setCreatedAt(new Timestamp(createdAt));
          qrCodeEntity.setUpdatedAt(new Timestamp(createdAt));
          qrCodeEntity.setStatus(ApaStatus.ACTIVE_QR_CODE);
          qrCodeRepository.save(qrCodeEntity);
        } catch (Exception e) {
          System.out.println("Something went wrong " + e.getMessage());
          throw new Error(e.getMessage());
        }
      });

      return new ResponseEntity<>("", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity countQrCode(int parkingId, int userId) {
    if (checkIfHavingAdminPermission(parkingId, userId)
        || checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        List<QrCodeEntity> codeList = qrCodeRepository.getAllAvailableQrCodeFromParkingId(parkingId);
        return new ResponseEntity<>(codeList.size(), HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity checkIn(int parkingId, int userId, String vehicleType, String plateNumber, String attachment,
      String description) {

    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        ParkingSpaceEntity parking = parkingSpaceRepository.getById(parkingId);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        boolean compareTime = compareTwoTimeStamps(currentTime, parking.getEndTime())
            || !compareTwoTimeStamps(currentTime, parking.getStartTime());
        if (compareTime) {
          return new ResponseEntity<>("Out of Working Hour", HttpStatus.BAD_REQUEST);
        }
        if (checkExistedParkedVehicle(plateNumber)) {
          return new ResponseEntity<>("This vehicle is already parked", HttpStatus.BAD_REQUEST);
        }
        List<QrCodeEntity> codeList = qrCodeRepository.getAllAvailableQrCodeFromParkingId(parkingId);

        if (codeList.isEmpty()) {
          return new ResponseEntity<>("This parking space is full", HttpStatus.NOT_ACCEPTABLE);
        }
        VehicleEntity existingVehicle;
        VehicleEntity newVehicle = new VehicleEntity();
        ParkingReservationEntity parkingReservation = new ParkingReservationEntity();

        Random rand = new Random();
        QrCodeEntity randomCode = codeList.get(rand.nextInt(codeList.size()));

        existingVehicle = vehicleRepository.findTop1ByPlateNumber(plateNumber);

        if (existingVehicle == null) {
          newVehicle.setPlateNumber(plateNumber);
          newVehicle.setVehicleType(vehicleType);
          parkingReservation.setVehicleId(vehicleRepository.save(newVehicle).getId());
        } else {
          parkingReservation.setVehicleId(existingVehicle.getId());
        }

        parkingReservation.setAttachment(attachment);
        parkingReservation.setStatus(ApaStatus.CHECK_IN);
        parkingReservation.setCodeId(randomCode.getId());
        parkingReservation.setParkingId(parkingId);
        parkingReservation.setDescription(description);
        parkingReservation.setQrCodeEntity(randomCode);
        parkingReservation.setParkingSpaceEntity(parking);

        ParkingReservationEntity res = parkingReservationRepository
            .getById(parkingReservationRepository.save(parkingReservation).getId());
        ParkingReservationActivityKey key = new ParkingReservationActivityKey(userId, res.getId(), ApaStatus.CHECK_IN);
        ParkingReservationActivityEntity activityEntity = new ParkingReservationActivityEntity(key,
            new Timestamp(System.currentTimeMillis()));
        parkingReservationActivityRepository.save(activityEntity);
        return new ResponseEntity<>(res, HttpStatus.OK);
      } catch (Exception e) {
        System.out.println("ParkingSpaceServiceImpl: CheckIn: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity getParkingReservationById(int parkingId, int parkingReservationId, int userId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      ParkingReservationEntity parkingRes = parkingReservationRepository
          .getById(parkingReservationId);
      List<ParkingReservationActivityEntity> activityEntity = parkingReservationActivityRepository
          .findAllByParkingReservationEntity(parkingRes);
      ParkingReservationResponse res = new ParkingReservationResponse(parkingRes, activityEntity);
      return new ResponseEntity<>(res, HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity getParkingReservationByCode(int parkingId, String code, int userId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        byte[] uniformCode = code.getBytes();

        QrCodeEntity codeEntity = qrCodeRepository.getByCode(uniformCode);

        ParkingReservationEntity parkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.CHECK_IN);
        List<ParkingReservationActivityEntity> activityEntity = parkingReservationActivityRepository
            .findAllByParkingReservationEntity(parkingRes);
        ParkingReservationResponse res = new ParkingReservationResponse(parkingRes, activityEntity);
        return new ResponseEntity<>(res, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }

    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public QrCodeEntity getQrCodeById(int codeId) {
    return qrCodeRepository.findById(codeId).orElse(null);
  }

  @Override
  public ResponseEntity getParkingReservationByExternalId(int parkingId, String externalId, int userId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        QrCodeEntity codeEntity = qrCodeRepository.findByExternalId(externalId).orElse(null);

        ParkingReservationEntity parkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.CHECK_IN);
        ParkingReservationEntity archivedParkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.ARCHIVED);
        if (parkingRes == null && archivedParkingRes == null) {
          return new ResponseEntity<>(new ApaMessage("No CheckIn Parking Reservation with This Code"),
              HttpStatus.BAD_REQUEST);
        } else if (parkingRes != null) {
          List<ParkingReservationActivityEntity> activityEntity = parkingReservationActivityRepository
              .findAllByParkingReservationEntity(parkingRes);
          ParkingReservationResponse res = new ParkingReservationResponse(parkingRes, activityEntity);
          return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
          List<ParkingReservationActivityEntity> activityEntity = parkingReservationActivityRepository
              .findAllByParkingReservationEntity(archivedParkingRes);
          ParkingReservationResponse res = new ParkingReservationResponse(archivedParkingRes, activityEntity);
          return new ResponseEntity<>(res, HttpStatus.OK);
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }

    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity checkOut(int parkingId, int parkingReservationId, String externalId, int userId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        ParkingReservationEntity reservation = parkingReservationRepository.findById(parkingReservationId).orElse(null);
        if (reservation == null) {
          return new ResponseEntity<>(new ApaMessage("Cannot find this parking reservation"), HttpStatus.OK);
        }
        reservation.setStatus(ApaStatus.CHECK_OUT);
        parkingReservationRepository.save(reservation);
        ParkingReservationActivityKey key = new ParkingReservationActivityKey(userId, reservation.getId(),
            ApaStatus.CHECK_OUT);
        ParkingReservationActivityEntity activityEntity = new ParkingReservationActivityEntity(key,
            new Timestamp(System.currentTimeMillis()));

        parkingReservationActivityRepository.save(activityEntity);

        return new ResponseEntity<>("", HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity getHistoryByParkingId(int userId, int parkingId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        return new ResponseEntity<>(parkingReservationActivityRepository.getHistoryByParkingId(parkingId),
            HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  private static String generateCronExpression(final Timestamp endTime) {
    return String.format("%1$s %2$s %3$s * * *", String.valueOf(endTime.getSeconds()),
        String.valueOf(endTime.getMinutes()), String.valueOf(endTime.getHours()));
  }

  private static String generate15MinutesCronExpression(final Timestamp endTime) {
    Timestamp cloneEndTime = new Timestamp(endTime.getTime());
    cloneEndTime.setTime(cloneEndTime.getTime() - TimeUnit.MINUTES.toMillis(15));

    return String.format("%1$s %2$s %3$s * * *", String.valueOf(cloneEndTime.getSeconds()),
        String.valueOf(cloneEndTime.getMinutes()), String.valueOf(cloneEndTime.getHours()));
  }

  @Override
  public List<ParkingSpaceCronJob> getCronJobArray() {
    List<ParkingSpaceEntity> parkingSpaceList = parkingSpaceRepository
        .getAllAvailableParkingSpace();

    List<ParkingSpaceCronJob> res = parkingSpaceList.stream()
        .map(p -> new ParkingSpaceCronJob(String.valueOf(p.getId()), generateCronExpression(p.getEndTime()), "2"))
        .collect(Collectors.toList());

    List<ParkingSpaceCronJob> secondRes = parkingSpaceList.stream()
        .map(p -> new ParkingSpaceCronJob(String.valueOf(p.getId()), generate15MinutesCronExpression(p.getEndTime()),
            "1"))
        .collect(Collectors.toList());
    res.add(new ParkingSpaceCronJob("-1", "0 * * * * *", "1"));

    List<ParkingSpaceCronJob> newList = Stream.concat(res.stream(), secondRes.stream())
        .collect(Collectors.toList());

    return newList;
  }

  @Override
  public ResponseEntity checkIfAvailableQrCode(int parkingId, String externalId, int userId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        QrCodeEntity codeEntity = qrCodeRepository.findByExternalId(externalId).orElse(null);
        if (codeEntity == null) {
          return new ResponseEntity<>("This QR Code doesn't exist", HttpStatus.OK);
        }
        if (Objects.equals(codeEntity.getStatus(), ApaStatus.DEACTIVE_QR_CODE)) {
          return new ResponseEntity<>("This QR Code Is Deactivated", HttpStatus.OK);
        }
        ParkingReservationEntity parkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.CHECK_IN);
        ParkingReservationEntity archivedParkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.ARCHIVED);
        if (parkingRes != null || archivedParkingRes != null) {
          return new ResponseEntity<>(new ApaMessage("Existed parking reservation with this code"),
              HttpStatus.OK);
        }

        return new ResponseEntity<>(codeEntity, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking reservation", HttpStatus.BAD_REQUEST);
    }
  }

  public boolean compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
    long min1 = oldTime.getHours() * 60 + oldTime.getMinutes();
    long min2 = currentTime.getHours() * 60 + currentTime.getMinutes();
    return min2 - min1 > 0;
  }

  @Override
  public ResponseEntity checkInWithCode(int parkingId, int userId, int codeId, String vehicleType,
      String plateNumber, String attachment, String description) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)) {
      try {
        ParkingSpaceEntity parking = parkingSpaceRepository.getById(parkingId);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        boolean compareTime = compareTwoTimeStamps(currentTime, parking.getEndTime())
            || !compareTwoTimeStamps(currentTime, parking.getStartTime());
        if (compareTime) {
          return new ResponseEntity<>("Out of Working Hour", HttpStatus.BAD_REQUEST);
        }
        if (checkExistedParkedVehicle(plateNumber)) {
          return new ResponseEntity<>("This vehicle is already parked", HttpStatus.BAD_REQUEST);
        }
        QrCodeEntity codeEntity = qrCodeRepository.findById(codeId).orElse(null);

        ParkingReservationEntity parkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.CHECK_IN);
        ParkingReservationEntity archivedParkingRes = parkingReservationRepository
            .getByQrCodeEntityAndStatus(codeEntity, ApaStatus.ARCHIVED);
        if (parkingRes != null || archivedParkingRes != null) {
          return new ResponseEntity<>("Existed parking reservation with this code",
              HttpStatus.BAD_REQUEST);
        }

        VehicleEntity existingVehicle;
        VehicleEntity newVehicle = new VehicleEntity();
        ParkingReservationEntity parkingReservation = new ParkingReservationEntity();

        existingVehicle = vehicleRepository.findTop1ByPlateNumber(plateNumber);

        if (existingVehicle == null) {
          newVehicle.setPlateNumber(plateNumber);
          newVehicle.setVehicleType(vehicleType);
          parkingReservation.setVehicleId(vehicleRepository.save(newVehicle).getId());
        }

        parkingReservation.setAttachment(attachment);
        parkingReservation.setStatus(ApaStatus.CHECK_IN);
        parkingReservation.setCodeId(codeId);
        parkingReservation.setParkingId(parkingId);
        parkingReservation.setDescription(description);
        parkingReservation.setQrCodeEntity(codeEntity);
        parkingReservation.setParkingSpaceEntity(parking);

        ParkingReservationEntity res = parkingReservationRepository
            .getById(parkingReservationRepository.save(parkingReservation).getId());
        ParkingReservationActivityKey key = new ParkingReservationActivityKey(userId, res.getId(), ApaStatus.CHECK_IN);
        ParkingReservationActivityEntity activityEntity = new ParkingReservationActivityEntity(key,
            new Timestamp(System.currentTimeMillis()));
        parkingReservationActivityRepository.save(activityEntity);
        return new ResponseEntity<>(res, HttpStatus.OK);
      } catch (Exception e) {
        System.out.println("ParkingSpaceServiceImpl: CheckIn: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
      }
    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity getAllCheckinParkingByParkingIdInRecentDay(int userId, int parkingId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)
        || checkIfHavingAdminPermission(parkingId, userId)) {
      try {
        ParkingSpaceEntity parking = parkingSpaceRepository.getById(parkingId);
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        String startTime = timeFormat.format(parking.getStartTime());
        String endTime = timeFormat.format(parking.getEndTime());
        String date = dateFormat.format(new Timestamp(System.currentTimeMillis()));

        Date parsedStartTime = parseDate.parse(date + " " + startTime);
        Date parsedEndTime = parseDate.parse(date + " " + endTime);

        Timestamp startTimestamp = new Timestamp(parsedStartTime.getTime());
        Timestamp endTimestamp = new Timestamp(parsedEndTime.getTime());

        System.out.println(startTimestamp);
        System.out.println(endTimestamp);
        List<ParkingReservationActivityEntity> res = parkingReservationActivityRepository
            .getParkinngResBetweenTimeByParkingId(parkingId, startTimestamp, endTimestamp, ApaStatus.CHECK_IN);
        if (res.isEmpty()) {
          return new ResponseEntity<>(Collections.emptyList(),
              HttpStatus.OK);
        }

        return new ResponseEntity<>(res,
            HttpStatus.OK);

      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity getAllBacklogParkingReservation(int userId, int parkingId) {
    // if (checkIfHavingParkingLotAttendantPermission(parkingId, userId)
    //     || checkIfHavingAdminPermission(parkingId, userId)) {
      try {

        List<ParkingReservationEntity> res = parkingReservationRepository
            .findAllByParkingIdAndStatus(parkingId, ApaStatus.CHECK_IN);
        if (res.isEmpty()) {
          return new ResponseEntity<>(Collections.emptyList(),
              HttpStatus.OK);
        }

        return new ResponseEntity<>(res,
            HttpStatus.OK);

      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

    // } else {
    //   return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    // }
  }

  @Override
  public int countAllBacklogParkingReservationByParkingId(int parkingId) {
    try {
      List<ParkingReservationEntity> res = parkingReservationRepository
          .findAllByParkingIdAndStatus(parkingId, ApaStatus.CHECK_IN);
      return res.size();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return -1;
    }
  }

  @Override
  public ResponseEntity getParkingSpaceById(int parkingId, int ownerId) {
    if (checkIfHavingParkingLotAttendantPermission(parkingId, ownerId)
        || checkIfHavingAdminPermission(parkingId, ownerId)) {
      try {
        ParkingSpaceEntity parkingSpaceEntity = parkingSpaceRepository.findById(parkingId).orElse(null);
        return new ResponseEntity<>(parkingSpaceEntity,
            HttpStatus.OK);

      } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

    } else {
      return new ResponseEntity<>("Cannot access this parking space", HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public void archiveAllBacklogVehicleByParkingId(int parkingId) {
    try {
      List<ParkingReservationEntity> res = parkingReservationRepository
          .findAllByParkingIdAndStatus(parkingId, ApaStatus.CHECK_IN);
      RoleEntity roleEntity = roleRepository.findTop1ByName(ApaRole.ROLE_SUPERADMMIN);
      UserEntity superAdmin = userRepository.findFirstByRoleByRoleId(roleEntity);

      for (int i = 0; i < res.size(); i++) {
        res.get(i).setStatus(ApaStatus.ARCHIVED);
        parkingReservationRepository.save(res.get(i));
        ParkingReservationActivityKey key = new ParkingReservationActivityKey(superAdmin.getId(), res.get(i).getId(),
            ApaStatus.ARCHIVED);
        ParkingReservationActivityEntity activityEntity = new ParkingReservationActivityEntity(key,
            new Timestamp(System.currentTimeMillis()));
        parkingReservationActivityRepository.save(activityEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
