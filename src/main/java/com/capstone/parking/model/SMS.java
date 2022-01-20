package com.capstone.parking.model;

import java.util.Objects;

public class SMS {
  private String phoneNumber;
  private String password;


  public SMS() {
  }

  public SMS(String phoneNumber, String password) {
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public SMS phoneNumber(String phoneNumber) {
    setPhoneNumber(phoneNumber);
    return this;
  }

  public SMS password(String password) {
    setPassword(password);
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SMS)) {
            return false;
        }
        SMS sMS = (SMS) o;
        return Objects.equals(phoneNumber, sMS.phoneNumber) && Objects.equals(password, sMS.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phoneNumber, password);
  }

  @Override
  public String toString() {
    return "{" +
      " phoneNumber='" + getPhoneNumber() + "'" +
      ", password='" + getPassword() + "'" +
      "}";
  }

}
