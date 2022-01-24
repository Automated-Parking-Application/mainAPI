package com.capstone.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "parking_space", schema = "apa")
public class ParkingSpaceEntity {
  private int id;
  private String name;
  private String address;
  private Timestamp startTime;
  private Timestamp endTime;
  private String description;
  private String image;
  private String status;
  private Integer ownerId;
  private UserEntity userEntity;

  public Timestamp getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

  public Timestamp getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

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
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
  @Column(name = "address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Basic
  @Column(name = "owner_id", updatable = false)
  @JsonIgnore
  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
  }

  @ManyToOne
  @JoinColumn(name = "owner_id", insertable = false, updatable = false)
  @JsonProperty("user")
  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ParkingSpaceEntity that = (ParkingSpaceEntity) o;
    return id == that.id &&
        Objects.equals(name, that.name) &&
        Objects.equals(address, that.address) &&
        Objects.equals(ownerId, that.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, address, ownerId);
  }
}
