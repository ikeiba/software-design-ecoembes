package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

public class AssignmentDTO {
  private LocalDate date;
  private String dumpsterId;
  private String employeeId;
  private String plantId;

  // Constructors
  public AssignmentDTO() {
  }

  public AssignmentDTO(LocalDate date, String dumpsterId, String employeeId, String plantId) {
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

  public String getDumpsterId() {
    return dumpsterId;
  }

  public void setDumpsterId(String dumpsterId) {
    this.dumpsterId = dumpsterId;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getPlantId() {
    return plantId;
  }

  public void setPlantId(String plantId) {
    this.plantId = plantId;
  }
}