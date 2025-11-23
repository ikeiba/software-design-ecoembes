package es.deusto.sd.ecoembes.dto;

public class NewDumpsterDTO {
  private String location;
  private String postalCode;
  private double initialCapacity; // As per source

  // Constructors
  public NewDumpsterDTO() {
  }

  public NewDumpsterDTO(String location, String postalCode, double initialCapacity) {
    this.location = location;
    this.postalCode = postalCode;
    this.initialCapacity = initialCapacity;
  }

  // Getters and Setters

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public double getInitialCapacity() {
    return initialCapacity;
  }

  public void setInitialCapacity(double initialCapacity) {
    this.initialCapacity = initialCapacity;
  }
}