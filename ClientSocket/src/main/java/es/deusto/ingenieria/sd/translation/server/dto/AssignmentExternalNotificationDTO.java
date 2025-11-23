package es.deusto.ingenieria.sd.translation.server.dto;

import java.io.Serializable;
import java.time.LocalDate;


public class AssignmentExternalNotificationDTO implements Serializable{
  private LocalDate date;
  private int nDumpster; //number of dumpsters being sent
  private int nContainer; //the combined number of containers they contain
  private String token;

  // Constructors
  public AssignmentExternalNotificationDTO() {
  }

  public AssignmentExternalNotificationDTO(LocalDate date, int nDumpster, int nContainer) {
    this.date = date;
    this.nDumpster = nDumpster; 
    this.nContainer = nContainer;


  }

  // Getters and Setters
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

 

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getnDumpster() {
    return nDumpster;
  }

  public void setnDumpster(int nDumpster) {
    this.nDumpster = nDumpster;
  }

  public int getnContainer() {
    return nContainer;
  }

  public void setnContainer(int nContainer) {
    this.nContainer = nContainer;
  }


}