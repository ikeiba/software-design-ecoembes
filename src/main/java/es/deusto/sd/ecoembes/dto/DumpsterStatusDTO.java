package es.deusto.sd.ecoembes.dto;

import es.deusto.sd.ecoembes.entity.FillLevel;

public class DumpsterStatusDTO {
  private String dumpsterId;
  private String location;
  private FillLevel currentStatus;

  // Constructors
  public DumpsterStatusDTO() {
  }

  public DumpsterStatusDTO(String dumpsterId, String location, FillLevel currentStatus) {
    this.dumpsterId = dumpsterId;
    this.location = location;
    this.currentStatus = currentStatus;
  }

  // Getters and Setters
  public String getDumpsterId() {
    return dumpsterId;
  }

  public void setDumpsterId(String dumpsterId) {
    this.dumpsterId = dumpsterId;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public FillLevel getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(FillLevel currentStatus) {
    this.currentStatus = currentStatus;
  }
}