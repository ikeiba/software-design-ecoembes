package es.deusto.sd.ecoembes.dto;

import es.deusto.sd.ecoembes.entity.FillLevel;

public class DumpsterStatusDTO {
    @Override
    public String toString() {
      return "DumpsterStatusDTO{" +
              "dumpsterId=" + dumpsterId +
              ", location='" + location + '\'' +
              ", currentStatus=" + currentStatus +
              '}';
    }
  private Long dumpsterId;
  private String location;
  private FillLevel currentStatus;

  // Constructors
  public DumpsterStatusDTO() {
  }

  public DumpsterStatusDTO(Long dumpsterId, String location, FillLevel currentStatus) {
    this.dumpsterId = dumpsterId;
    this.location = location;
    this.currentStatus = currentStatus;
  }

  // Getters and Setters
  public Long getDumpsterId() {
    return dumpsterId;
  }

  public void setDumpsterId(Long dumpsterId) {
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