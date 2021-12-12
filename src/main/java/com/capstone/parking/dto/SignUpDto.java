package com.capstone.parking.dto;

import lombok.Data;

@Data
public class SignUpDto {
  private String phoneNumber;
  private String password;
  private String fullName;
  private String address;
}
