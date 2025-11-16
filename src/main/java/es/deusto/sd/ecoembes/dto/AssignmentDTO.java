package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

public class AssignmentDTO {
  private LocalDate date;
  private Long dumpsterId;
  private Long employeeId;
  private Long plantId;

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
}