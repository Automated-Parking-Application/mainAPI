package com.capstone.parking.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParkingSpaceAttendantKey implements Serializable {

    @Column(name = "user_id")
    int userId;

    @Column(name = "parking_id")
    int parkingId;


    public ParkingSpaceAttendantKey() {
    }

    public ParkingSpaceAttendantKey(int userId, int parkingId) {
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParkingId() {
        return this.parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public ParkingSpaceAttendantKey userId(int userId) {
        setUserId(userId);
        return this;
    }

    public ParkingSpaceAttendantKey parkingId(int parkingId) {
        setParkingId(parkingId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParkingSpaceAttendantKey)) {
            return false;
        }
        ParkingSpaceAttendantKey parkingSpaceAttendantKey = (ParkingSpaceAttendantKey) o;
        return userId == parkingSpaceAttendantKey.userId && parkingId == parkingSpaceAttendantKey.parkingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, parkingId);
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", parkingId='" + getParkingId() + "'" +
            "}";
    }

}
