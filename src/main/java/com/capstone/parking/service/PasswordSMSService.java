package com.capstone.parking.service;

import com.capstone.parking.model.SMS;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordSMSService {
  @Value("${twilio.account-sid}")
  private String ACCOUNT_SID;

  @Value("${twilio.auth-token}")
  private String AUTH_TOKEN;

  @Value("${twilio.message-service-sid}")
  private String MESSAGE_SERVICE_SID;

  public boolean send(SMS sms) {
    try {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      Message message = Message.creator(
          new com.twilio.type.PhoneNumber(sms.getPhoneNumber()),
          MESSAGE_SERVICE_SID,
          "Your password to QPA with phone number " + sms.getPhoneNumber() + " is " + sms.getPassword())
          .create();
      System.out.println("Send " + sms.getPassword());
      if (message.getStatus() != Status.ACCEPTED) {
        return false;
      }
    } catch (Exception e) {
      System.out.println("Something went wrong" + e.getMessage());
      return false;
    }
    return true;
  }
}
