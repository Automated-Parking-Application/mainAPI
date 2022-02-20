package com.capstone.parking.entity;

import com.capstone.parking.constants.ApaStatus;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "parking_reservation_activity", schema = "apa")
public class ParkingReservationActivityEntity implements Serializable {
  @EmbeddedId
  private ParkingReservationActivityKey id;

  @ManyToOne
  @MapsId("user_id")
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @MapsId("parking_reservation_id")
  @JoinColumn(name = "parking_reservation_id")
  private ParkingReservationEntity parkingReservationEntity;

  @Basic
  @Column(name = "type")
  private String type;

  @Basic
  @Column(name = "created_at")
  private Timestamp createTime;

  public ParkingReservationActivityEntity() {
  }

  public ParkingReservationActivityEntity(ParkingReservationActivityKey id, UserEntity user,
      ParkingReservationEntity parkingReservationEntity, String type, Timestamp createTime) {
    this.id = id;
    this.user = user;
    this.parkingReservationEntity = parkingReservationEntity;
    this.type = type;
    this.createTime = createTime;
  }

  public ParkingReservationActivityEntity(ParkingReservationActivityKey id, String type, Timestamp createTime) {
    this.id = id;
    this.type = type;
    this.createTime = createTime;
  }

  public ParkingReservationActivityKey getId() {
    return this.id;
  }

  public void setId(ParkingReservationActivityKey id) {
    this.id = id;
  }

  public UserEntity getUser() {
    return this.user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public ParkingReservationEntity getParkingReservationEntity() {
    return this.parkingReservationEntity;
  }

  public void setParkingReservationEntity(ParkingReservationEntity parkingReservationEntity) {
    this.parkingReservationEntity = parkingReservationEntity;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Timestamp getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public ParkingReservationActivityEntity id(ParkingReservationActivityKey id) {
    setId(id);
    return this;
  }

  public ParkingReservationActivityEntity user(UserEntity user) {
    setUser(user);
    return this;
  }

  public ParkingReservationActivityEntity parkingReservationEntity(ParkingReservationEntity parkingReservationEntity) {
    setParkingReservationEntity(parkingReservationEntity);
    return this;
  }

  public ParkingReservationActivityEntity type(String type) {
    setType(type);
    return this;
  }

  public ParkingReservationActivityEntity createTime(Timestamp createTime) {
    setCreateTime(createTime);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof ParkingReservationActivityEntity)) {
      return false;
    }
    ParkingReservationActivityEntity parkingReservationActivityEntity = (ParkingReservationActivityEntity) o;
    return Objects.equals(id, parkingReservationActivityEntity.id)
        && Objects.equals(user, parkingReservationActivityEntity.user)
        && Objects.equals(parkingReservationEntity, parkingReservationActivityEntity.parkingReservationEntity)
        && Objects.equals(type, parkingReservationActivityEntity.type)
        && Objects.equals(createTime, parkingReservationActivityEntity.createTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, parkingReservationEntity, type, createTime);
  }

  @Override
  public String toString() {
    return "{" +
        " id='" + getId() + "'" +
        ", user='" + getUser() + "'" +
        ", parkingReservationEntity='" + getParkingReservationEntity() + "'" +
        ", type='" + getType() + "'" +
        ", createTime='" + getCreateTime() + "'" +
        "}";
  }

}
