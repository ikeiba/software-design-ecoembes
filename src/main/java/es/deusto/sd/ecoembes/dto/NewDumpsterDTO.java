package es.deusto.sd.ecoembes.dto;

public class NewDumpsterDTO {
  private String dumpsterId;
  private String location;
  private double initialCapacity; // As per source

  // Constructors
  public NewDumpsterDTO() {
  }

  public NewDumpsterDTO(String dumpsterId, String location, double initialCapacity) {
    this.dumpsterId = dumpsterId;
    this.location = location;
    this.initialCapacity = initialCapacity;
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

  public double getInitialCapacity() {
    return initialCapacity;
  }

  public void setInitialCapacity(double initialCapacity) {
    this.initialCapacity = initialCapacity;
  }
}