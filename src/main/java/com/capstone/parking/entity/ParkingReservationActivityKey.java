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

  public ParkingReservationActivityKey() {
  }

  public ParkingReservationActivityKey(int userId, int parkingReservationId) {
    this.userId = userId;
    this.parkingReservationId = parkingReservationId;
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

  public ParkingReservationActivityKey userId(int userId) {
    setUserId(userId);
    return this;
  }

  public ParkingReservationActivityKey parkingReservationId(int parkingReservationId) {
    setParkingReservationId(parkingReservationId);
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
        return userId == parkingReservationActivityKey.userId && parkingReservationId == parkingReservationActivityKey.parkingReservationId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, parkingReservationId);
  }

  @Override
  public String toString() {
    return "{" +
      " userId='" + getUserId() + "'" +
      ", parkingReservationId='" + getParkingReservationId() + "'" +
      "}";
  }


}
