package es.deusto.sd.ecoembes.dto;

import es.deusto.sd.ecoembes.entity.FillLevel;

public class DumpsterUpdateDTO {
  private Long dumpsterId;
  private int estimatedContainers;
  private FillLevel fillLevel; // Using an Enum is better design

  // Constructors
  public DumpsterUpdateDTO() {
  }

  public DumpsterUpdateDTO(Long dumpsterId, int estimatedContainers, FillLevel fillLevel) {
    this.dumpsterId = dumpsterId;
    this.estimatedContainers = estimatedContainers;
    this.fillLevel = fillLevel;
  }

  // Getters and Setters
  public Long getDumpsterId() {
    return dumpsterId;
  }

  public void setDumpsterId(Long dumpsterId) {
    this.dumpsterId = dumpsterId;
  }

  public int getEstimatedContainers() {
    return estimatedContainers;
  }

  public void setEstimatedContainers(int estimatedContainers) {
    this.estimatedContainers = estimatedContainers;
  }

  public FillLevel getFillLevel() {
    return fillLevel;
  }

  public void setFillLevel(FillLevel fillLevel) {
    this.fillLevel = fillLevel;
  }
}