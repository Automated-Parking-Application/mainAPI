package com.capstone.parking.wrapper;
import com.capstone.parking.entity.ParkingReservationActivityEntity;
import com.capstone.parking.entity.ParkingReservationEntity;
import java.util.List;
import java.util.Objects;

public class ParkingReservationResponse {
  private ParkingReservationEntity parkingReservation;
  private List<ParkingReservationActivityEntity> parkingReservationActivity;

  public ParkingReservationResponse() {
  }

  public ParkingReservationResponse(ParkingReservationEntity parkingReservation, List<ParkingReservationActivityEntity> parkingReservationActivity) {
    this.parkingReservation = parkingReservation;
    this.parkingReservationActivity = parkingReservationActivity;
  }

  public ParkingReservationEntity getParkingReservation() {
    return this.parkingReservation;
  }

  public void setParkingReservation(ParkingReservationEntity parkingReservation) {
    this.parkingReservation = parkingReservation;
  }

  public List<ParkingReservationActivityEntity> getParkingReservationActivity() {
    return this.parkingReservationActivity;
  }

  public void setParkingReservationActivity(List<ParkingReservationActivityEntity> parkingReservationActivity) {
    this.parkingReservationActivity = parkingReservationActivity;
  }

  public ParkingReservationResponse parkingReservation(ParkingReservationEntity parkingReservation) {
    setParkingReservation(parkingReservation);
    return this;
  }

  public ParkingReservationResponse parkingReservationActivity(List<ParkingReservationActivityEntity> parkingReservationActivity) {
    setParkingReservationActivity(parkingReservationActivity);
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParkingReservationResponse)) {
            return false;
        }
        ParkingReservationResponse parkingReservationResponse = (ParkingReservationResponse) o;
        return Objects.equals(parkingReservation, parkingReservationResponse.parkingReservation) && Objects.equals(parkingReservationActivity, parkingReservationResponse.parkingReservationActivity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parkingReservation, parkingReservationActivity);
  }

  @Override
  public String toString() {
    return "{" +
      " parkingReservation='" + getParkingReservation() + "'" +
      ", parkingReservationActivity='" + getParkingReservationActivity() + "'" +
      "}";
  }



}
