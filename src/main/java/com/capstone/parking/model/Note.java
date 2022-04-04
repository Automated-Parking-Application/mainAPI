package com.capstone.parking.model;

import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class Note {
  private String subject;
  private String content;
  private Map<String, String> data;
  private String image;


  public Note() {
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Map<String,String> getData() {
    return this.data;
  }

  public void setData(Map<String,String> data) {
    this.data = data;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Note subject(String subject) {
    setSubject(subject);
    return this;
  }

  public Note content(String content) {
    setContent(content);
    return this;
  }

  public Note data(Map<String,String> data) {
    setData(data);
    return this;
  }

  public Note image(String image) {
    setImage(image);
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Note)) {
            return false;
        }
        Note note = (Note) o;
        return Objects.equals(subject, note.subject) && Objects.equals(content, note.content) && Objects.equals(data, note.data) && Objects.equals(image, note.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, content, data, image);
  }

  @Override
  public String toString() {
    return "{" +
      " subject='" + getSubject() + "'" +
      ", content='" + getContent() + "'" +
      ", data='" + getData() + "'" +
      ", image='" + getImage() + "'" +
      "}";
  }


  public Note(String subject, String content, Map<String,String> data, String image) {
    this.subject = subject;
    this.content = content;
    this.data = data;
    this.image = image;
  }

}
