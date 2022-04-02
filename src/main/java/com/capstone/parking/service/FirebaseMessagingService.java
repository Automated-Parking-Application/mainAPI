package com.capstone.parking.service;

import com.capstone.parking.model.Note;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

  private final FirebaseMessaging firebaseMessaging;

  public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
    this.firebaseMessaging = firebaseMessaging;
  }

  public String sendNotificationToATopic(Note note, String topic) throws FirebaseMessagingException {

    Notification notification = Notification
        .builder()
        .setTitle(note.getSubject())
        .setBody(note.getContent())
        .setImage(note.getImage())
        .build();

    Message message = Message
        .builder()
        .setTopic(topic)
        .setNotification(notification)
        .putAllData(note.getData())
        .build();

    return firebaseMessaging.send(message);
  }

}
