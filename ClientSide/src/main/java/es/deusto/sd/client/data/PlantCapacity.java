package es.deusto.sd.client.data;


public class PlantCapacity{ //we make it serializable so that it can be send as output or input in sockets
  private Long plantId;
  private String plantName;
  private double availableCapacityTons;

  // Constructors
  public PlantCapacity() {
  }

  public PlantCapacity(Long plantId, String plantName, double availableCapacityTons) {
    this.plantId = plantId;
    this.plantName = plantName;
    this.availableCapacityTons = availableCapacityTons;
  }
  
  public String getPlantName() {
    return plantName;
  }

  public void setPlantName(String plantName) {
    this.plantName = plantName;
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