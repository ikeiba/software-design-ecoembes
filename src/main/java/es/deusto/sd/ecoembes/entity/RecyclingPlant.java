package es.deusto.sd.ecoembes.entity;

public class RecyclingPlant {

  private String plantId; // e.g., "PlasSB", "ContSocket"
  private String name; // e.g., "PlasSB Ltd."

  // Constructors
  public RecyclingPlant() {
  }

  public RecyclingPlant(String plantId, String name) {
    this.plantId = plantId;
    this.name = name;
  }

  // Getters and Setters
  public String getPlantId() {
    return plantId;
  }

  public void setPlantId(String plantId) {
    this.plantId = plantId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}