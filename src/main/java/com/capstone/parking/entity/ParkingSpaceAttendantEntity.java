package com.capstone.parking.entity;

import com.capstone.parking.constants.ApaStatus;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "parking_space_attendant",schema = "apa")
public class ParkingSpaceAttendantEntity implements Serializable {

    @EmbeddedId
    private ParkingSpaceAttendantKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("parking_id")
    @JoinColumn(name = "parking_id")
    private ParkingSpaceEntity parkingSpace;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "created_at")
    private Timestamp createTime;

    @Basic
    @Column(name = "updated_at")
    private Timestamp modifyTime;

    public ParkingSpaceAttendantEntity() {
    }

    public ParkingSpaceAttendantEntity(ParkingSpaceAttendantKey id) {
        this.id = id;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.modifyTime = new Timestamp(System.currentTimeMillis());
        this.status = ApaStatus.ACTIVE_PARKING_SPACE_ATTENDANT;
    }

    public ParkingSpaceAttendantEntity id(ParkingSpaceAttendantKey id) {
        setId(id);
        return this;
    }

    public ParkingSpaceAttendantEntity user(UserEntity user) {
        setUser(user);
        return this;
    }

    public ParkingSpaceAttendantEntity parkingSpace(ParkingSpaceEntity parkingSpace) {
        setParkingSpace(parkingSpace);
        return this;
    }

    public ParkingSpaceAttendantEntity status(String status) {
        setStatus(status);
        return this;
    }

    public ParkingSpaceAttendantEntity createTime(Timestamp createTime) {
        setCreateTime(createTime);
        return this;
    }

    public ParkingSpaceAttendantEntity modifyTime(Timestamp modifyTime) {
        setModifyTime(modifyTime);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParkingSpaceAttendantEntity)) {
            return false;
        }
        ParkingSpaceAttendantEntity parkingSpaceAttendantEntity = (ParkingSpaceAttendantEntity) o;
        return Objects.equals(id, parkingSpaceAttendantEntity.id)
                && Objects.equals(user, parkingSpaceAttendantEntity.user)
                && Objects.equals(parkingSpace, parkingSpaceAttendantEntity.parkingSpace)
                && Objects.equals(status, parkingSpaceAttendantEntity.status)
                && Objects.equals(createTime, parkingSpaceAttendantEntity.createTime)
                && Objects.equals(modifyTime, parkingSpaceAttendantEntity.modifyTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, parkingSpace, status, createTime, modifyTime);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", user='" + getUser() + "'" +
                ", parkingSpace='" + getParkingSpace() + "'" +
                ", status='" + getStatus() + "'" +
                ", createTime='" + getCreateTime() + "'" +
                ", modifyTime='" + getModifyTime() + "'" +
                "}";
    }

    public ParkingSpaceAttendantKey getId() {
        return this.id;
    }

    public void setId(ParkingSpaceAttendantKey id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ParkingSpaceEntity getParkingSpace() {
        return this.parkingSpace;
    }

    public void setParkingSpace(ParkingSpaceEntity parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

}
