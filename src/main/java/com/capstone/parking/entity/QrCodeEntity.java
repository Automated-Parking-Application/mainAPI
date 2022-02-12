package com.capstone.parking.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.capstone.parking.constants.ApaStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "qr_code", schema = "apa")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class QrCodeEntity implements Serializable {
  private int id;
  private byte[] code;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String status;
  private ParkingSpaceEntity parkingSpaceEntity;
  private int parkingId;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "code")
  public byte[] getCode() {
    return code;
  }

  public void setCode(byte[] code) {
    this.code = code;
  }

  @ManyToOne
  @JoinColumn(name = "parking_id", insertable = false, updatable = false)
  @JsonProperty("parkingSpace")
  public ParkingSpaceEntity getParkingSpaceEntity() {
    return parkingSpaceEntity;
  }

  public void setParkingSpace(ParkingSpaceEntity parkingSpaceEntity) {
    this.parkingSpaceEntity = parkingSpaceEntity;
  }

  @Basic
  @Column(name = "parking_id", updatable = false)
  @JsonIgnore
  public int getParkingId() {
    return parkingId;
  }

  public void setParkingId(int parkingId) {
    this.parkingId = parkingId;
  }

  public QrCodeEntity() {
  }

  public QrCodeEntity(byte[] code,
      int parkingId, Timestamp createdAt, Timestamp updatedAt) {
    this.code = code;
    this.status = ApaStatus.ACTIVE_QR_CODE;
    this.parkingId = parkingId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  @Basic
  @Column(name = "created_at")
  public Timestamp getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  @Basic
  @Column(name = "updated_at")
  public Timestamp getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setParkingSpaceEntity(ParkingSpaceEntity parkingSpaceEntity) {
    this.parkingSpaceEntity = parkingSpaceEntity;
  }

  public QrCodeEntity id(int id) {
    setId(id);
    return this;
  }

  public QrCodeEntity code(byte[] code) {
    setCode(code);
    return this;
  }

  public QrCodeEntity createdAt(Timestamp createdAt) {
    setCreatedAt(createdAt);
    return this;
  }

  public QrCodeEntity updatedAt(Timestamp updatedAt) {
    setUpdatedAt(updatedAt);
    return this;
  }

  public QrCodeEntity status(String status) {
    setStatus(status);
    return this;
  }

  public QrCodeEntity parkingSpaceEntity(ParkingSpaceEntity parkingSpaceEntity) {
    setParkingSpaceEntity(parkingSpaceEntity);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof QrCodeEntity)) {
      return false;
    }
    QrCodeEntity qrCodeEntity = (QrCodeEntity) o;
    return id == qrCodeEntity.id && Objects.equals(code, qrCodeEntity.code)
        && Objects.equals(createdAt, qrCodeEntity.createdAt) && Objects.equals(updatedAt, qrCodeEntity.updatedAt)
        && Objects.equals(status, qrCodeEntity.status)
        && Objects.equals(parkingSpaceEntity, qrCodeEntity.parkingSpaceEntity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, createdAt, updatedAt, status, parkingSpaceEntity);
  }

  @Override
  public String toString() {
    return "{" +
        " id='" + getId() + "'" +
        ", code='" + getCode() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        ", status='" + getStatus() + "'" +
        ", parkingSpaceEntity='" + getParkingSpaceEntity() + "'" +
        "}";
  }

}
