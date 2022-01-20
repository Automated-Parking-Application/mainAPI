package com.capstone.parking.service;

import com.capstone.parking.model.SMS;
import com.capstone.parking.utilities.ApaMessage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PasswordSMSService {
  @Value("${twilio.account-sid}")
  private String ACCOUNT_SID;

  @Value("${twilio.auth-token}")
  private String AUTH_TOKEN;

  @Value("${twilio.message-service-sid}")
  private String MESSAGE_SERVICE_SID;

  public ResponseEntity send(SMS sms) {
    try {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      Message message = Message.creator(
          new com.twilio.type.PhoneNumber(sms.getPhoneNumber()),
          MESSAGE_SERVICE_SID,
          "Your password to QPA with phone number " + sms.getPhoneNumber() + " is " + sms.getPassword())
          .create();
      if (message.getStatus() != Status.ACCEPTED) {
        return new ResponseEntity<>(new ApaMessage("Something went wrong"), HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new ApaMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
