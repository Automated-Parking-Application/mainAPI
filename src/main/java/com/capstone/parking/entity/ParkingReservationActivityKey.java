package com.capstone.parking.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParkingReservationActivityKey implements Serializable {

  @Column(name = "user_id")
  int userId;

  @Column(name = "parking_reservation_id")
  int parkingReservationId;

  @Column(name = "type")
  String type;

  public ParkingReservationActivityKey() {
  }

  public ParkingReservationActivityKey(int userId, int parkingReservationId, String type) {
    this.userId = userId;
    this.parkingReservationId = parkingReservationId;
    this.type = type;
  }

  public int getUserId() {
    return this.userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getParkingReservationId() {
    return this.parkingReservationId;
  }

  public void setParkingReservationId(int parkingReservationId) {
    this.parkingReservationId = parkingReservationId;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ParkingReservationActivityKey userId(int userId) {
    setUserId(userId);
    return this;
  }

  public ParkingReservationActivityKey parkingReservationId(int parkingReservationId) {
    setParkingReservationId(parkingReservationId);
    return this;
  }

  public ParkingReservationActivityKey type(String type) {
    setType(type);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof ParkingReservationActivityKey)) {
      return false;
    }
    ParkingReservationActivityKey parkingReservationActivityKey = (ParkingReservationActivityKey) o;
    return userId == parkingReservationActivityKey.userId
        && parkingReservationId == parkingReservationActivityKey.parkingReservationId
        && Objects.equals(type, parkingReservationActivityKey.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, parkingReservationId, type);
  }

  @Override
  public String toString() {
    return "{" +
        " userId='" + getUserId() + "'" +
        ", parkingReservationId='" + getParkingReservationId() + "'" +
        ", type='" + getType() + "'" +
        "}";
  }

}
