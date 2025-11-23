package es.deusto.sd.ecoembes.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignmentDTO implements Serializable{
  private LocalDate date;
  private Long dumpsterId;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long employeeId;
  private Long plantId;
  private String token;

  // Constructors
  public AssignmentDTO() {
  }

  public AssignmentDTO(LocalDate date, Long dumpsterId, Long employeeId, Long plantId) {
    this.date = date;
    this.dumpsterId = dumpsterId;
    this.employeeId = employeeId;
    this.plantId = plantId;
  }

  // Getters and Setters
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getDumpsterId() {
    return dumpsterId;
  }

  public void setDumpsterId(Long dumpsterId) {
    this.dumpsterId = dumpsterId;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public Long getPlantId() {
    return plantId;
  }

  public void setPlantId(Long plantId) {
    this.plantId = plantId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}