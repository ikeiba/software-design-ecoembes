package es.deusto.sd.ecoembes.dto;

import java.io.Serializable;

public class PlantCapacityDTO implements Serializable { //we make it serializable so that it can be send as output or input in sockets
  private Long plantId;
  private double availableCapacityTons;

  // Constructors
  public PlantCapacityDTO() {
  }

  public PlantCapacityDTO(Long plantId, double availableCapacityTons) {
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