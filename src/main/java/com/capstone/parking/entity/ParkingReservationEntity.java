package com.capstone.parking.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "parking_reservation",schema = "apa")
public class ParkingReservationEntity implements Serializable{
  private int id;
  private String attachment;
  private String status;
  private ParkingSpaceEntity parkingSpaceEntity;
  private int parkingId;
  private QrCodeEntity qrCodeEntity;
  private int codeId;
  private VehicleEntity vehicleEntity;
  private int vehicleId;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public ParkingReservationEntity() {
  }

  public ParkingReservationEntity(int id, String attachment, String status, ParkingSpaceEntity parkingSpaceEntity, int parkingId, QrCodeEntity qrCodeEntity, int codeId, VehicleEntity vehicleEntity, int vehicleId) {
    this.id = id;
    this.attachment = attachment;
    this.status = status;
    this.parkingSpaceEntity = parkingSpaceEntity;
    this.parkingId = parkingId;
    this.qrCodeEntity = qrCodeEntity;
    this.codeId = codeId;
    this.vehicleEntity = vehicleEntity;
    this.vehicleId = vehicleId;
  }

  @Basic
  @Column(name = "attachment")
  public String getAttachment() {
    return this.attachment;
  }

  public void setAttachment(String attachment) {
    this.attachment = attachment;
  }

  @Basic
  @Column(name = "status")
  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @ManyToOne
  @JoinColumn(name = "parking_id", insertable = false, updatable = false)
  @JsonProperty("parking_space")
  public ParkingSpaceEntity getParkingSpaceEntity() {
    return this.parkingSpaceEntity;
  }

  public void setParkingSpaceEntity(ParkingSpaceEntity parkingSpaceEntity) {
    this.parkingSpaceEntity = parkingSpaceEntity;
  }

  @Basic
  @Column(name = "parking_id", updatable = false)
  public int getParkingId() {
    return this.parkingId;
  }

  public void setParkingId(int parkingId) {
    this.parkingId = parkingId;
  }

  @ManyToOne
  @JoinColumn(name = "code_id", insertable = false, updatable = false)
  @JsonProperty("code")
  public QrCodeEntity getQrCodeEntity() {
    return this.qrCodeEntity;
  }

  public void setQrCodeEntity(QrCodeEntity qrCodeEntity) {
    this.qrCodeEntity = qrCodeEntity;
  }

  @Basic
  @Column(name = "code_id", updatable = false)
  public int getCodeId() {
    return this.codeId;
  }

  public void setCodeId(int codeId) {
    this.codeId = codeId;
  }

  @ManyToOne
  @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
  @JsonProperty("vehicle")
  public VehicleEntity getVehicleEntity() {
    return this.vehicleEntity;
  }

  public void setVehicleEntity(VehicleEntity vehicleEntity) {
    this.vehicleEntity = vehicleEntity;
  }

  @Basic
  @Column(name = "vehicle_id", updatable = false)
  @JsonIgnore
  public int getVehicleId() {
    return this.vehicleId;
  }

  public void setVehicleId(int vehicleId) {
    this.vehicleId = vehicleId;
  }


  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParkingReservationEntity)) {
            return false;
        }
        ParkingReservationEntity parkingReservationEntity = (ParkingReservationEntity) o;
        return id == parkingReservationEntity.id && Objects.equals(attachment, parkingReservationEntity.attachment) && Objects.equals(status, parkingReservationEntity.status) && Objects.equals(parkingSpaceEntity, parkingReservationEntity.parkingSpaceEntity) && parkingId == parkingReservationEntity.parkingId && Objects.equals(qrCodeEntity, parkingReservationEntity.qrCodeEntity) && codeId == parkingReservationEntity.codeId && Objects.equals(vehicleEntity, parkingReservationEntity.vehicleEntity) && vehicleId == parkingReservationEntity.vehicleId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, attachment, status, parkingSpaceEntity, parkingId, qrCodeEntity, codeId, vehicleEntity, vehicleId);
  }

  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", attachment='" + getAttachment() + "'" +
      ", status='" + getStatus() + "'" +
      ", parkingSpaceEntity='" + getParkingSpaceEntity() + "'" +
      ", parkingId='" + getParkingId() + "'" +
      ", qrCodeEntity='" + getQrCodeEntity() + "'" +
      ", codeId='" + getCodeId() + "'" +
      ", vehicleEntity='" + getVehicleEntity() + "'" +
      ", vehicleId='" + getVehicleId() + "'" +
      "}";
  }

}
