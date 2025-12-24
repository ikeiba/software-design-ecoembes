package es.deusto.sd.client.data;


public class PlantCapacity { 
  private Long plantId;
  private double availableCapacityTons;

  // Constructors
  public PlantCapacity() {
  }

  public PlantCapacity(Long plantId, double availableCapacityTons) {
    this.plantId = plantId;
    this.availableCapacityTons = availableCapacityTons;
  }

  // Getters and Setters
  public Long getPlantId() {
    return plantId;
  }

  public void setPlantId(Long plantId) {
    this.plantId = plantId;
  }

  public double getAvailableCapacityTons() {
    return availableCapacityTons;
  }

  public void setAvailableCapacityTons(double availableCapacityTons) {
    this.availableCapacityTons = availableCapacityTons;
  }
}