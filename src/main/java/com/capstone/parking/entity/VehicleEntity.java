package com.capstone.parking.entity;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "vehicle", schema = "apa")
public class VehicleEntity implements Serializable {
  private int id;
  private String plateNumber;
  private String vehicleType;

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
  @Column(name = "plate_number")
  public String getPlateNumber() {
    return plateNumber;
  }

  public void setPlateNumber(String plateNumber) {
    this.plateNumber = plateNumber;
  }

  @Basic
  @Column(name = "vehicle_type")
  public String getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(String vehicleType) {
    this.vehicleType = vehicleType;
  }


  public VehicleEntity() {
  }

  public VehicleEntity(int id, String plateNumber, String vehicleType) {
    this.id = id;
    this.plateNumber = plateNumber;
    this.vehicleType = vehicleType;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VehicleEntity)) {
            return false;
        }
        VehicleEntity vehicleEntity = (VehicleEntity) o;
        return id == vehicleEntity.id && Objects.equals(plateNumber, vehicleEntity.plateNumber) && Objects.equals(vehicleType, vehicleEntity.vehicleType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, plateNumber, vehicleType);
  }

  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", plateNumber='" + getPlateNumber() + "'" +
      ", vehicleType='" + getVehicleType() + "'" +
      "}";
  }

}
