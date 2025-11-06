package es.deusto.sd.ecoembes.dto;

public class DumpsterDTO {
  private String dumpsterId;
  private String location;
  private double capacity;

  // Constructors
  public DumpsterDTO() {
  }

  public DumpsterDTO(String dumpsterId, String location, double capacity) {
    this.dumpsterId = dumpsterId;
    this.location = location;
    this.capacity = capacity;
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

  public double getCapacity() {
    return capacity;
  }

  public void setCapacity(double capacity) {
    this.capacity = capacity;
  }
}