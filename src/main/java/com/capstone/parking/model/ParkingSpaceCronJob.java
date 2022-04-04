package com.capstone.parking.model;

import java.util.Objects;

public class ParkingSpaceCronJob {
  private String id;
  private String cronExpression;

  public ParkingSpaceCronJob() {
  }

  public ParkingSpaceCronJob(String id, String cronExpression) {
    this.id = id;
    this.cronExpression = cronExpression;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCronExpression() {
    return this.cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public ParkingSpaceCronJob id(String id) {
    setId(id);
    return this;
  }

  public ParkingSpaceCronJob cronExpression(String cronExpression) {
    setCronExpression(cronExpression);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof ParkingSpaceCronJob)) {
      return false;
    }
    ParkingSpaceCronJob parkingSpaceCronJob = (ParkingSpaceCronJob) o;
    return Objects.equals(id, parkingSpaceCronJob.id)
        && Objects.equals(cronExpression, parkingSpaceCronJob.cronExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cronExpression);
  }

  @Override
  public String toString() {
    return "{" +
        " id='" + getId() + "'" +
        ", cronExpression='" + getCronExpression() + "'" +
        "}";
  }

}
