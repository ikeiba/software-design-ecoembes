package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

import es.deusto.sd.ecoembes.entity.FillLevel;

public class DumpsterUsageDTO {
  private LocalDate date;
  private int estimatedContainers;
  private FillLevel fillLevel;

  // Constructors
  public DumpsterUsageDTO() {
  }

  public DumpsterUsageDTO(LocalDate date, int estimatedContainers, FillLevel fillLevel) {
    this.date = date;
    this.estimatedContainers = estimatedContainers;
    this.fillLevel = fillLevel;
  }

  // Getters and Setters
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
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