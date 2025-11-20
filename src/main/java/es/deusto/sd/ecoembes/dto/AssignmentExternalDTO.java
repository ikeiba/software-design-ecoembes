package es.deusto.sd.ecoembes.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.deusto.sd.ecoembes.entity.Dumpster;

public class AssignmentExternalDTO implements Serializable{
  private LocalDate date;
  private Dumpster dumpster;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String token;

  // Constructors
  public AssignmentExternalDTO() {
  }

  public AssignmentExternalDTO(LocalDate date, Dumpster dumpster) {
    this.date = date;
    this.dumpster = dumpster;

  }

  // Getters and Setters
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Dumpster getDumpster() {
    return dumpster;
  }

  public void setDumpster(Dumpster dumpster) {
    this.dumpster = dumpster;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}