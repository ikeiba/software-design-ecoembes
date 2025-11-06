package es.deusto.sd.ecoembes.dto;

public class PlantCapacityDTO {
  private String plantId; // e.g., "PlasSB", "ContSocket"
  private double availableCapacityTons;

  // Constructors
  public PlantCapacityDTO() {
  }

  public PlantCapacityDTO(String plantId, double availableCapacityTons) {
    this.plantId = plantId;
    this.availableCapacityTons = availableCapacityTons;
  }

  // Getters and Setters
  public String getPlantId() {
    return plantId;
  }

  public void setPlantId(String plantId) {
    this.plantId = plantId;
  }

  public double getAvailableCapacityTons() {
    return availableCapacityTons;
  }

  public void setAvailableCapacityTons(double availableCapacityTons) {
    this.availableCapacityTons = availableCapacityTons;
  }
}