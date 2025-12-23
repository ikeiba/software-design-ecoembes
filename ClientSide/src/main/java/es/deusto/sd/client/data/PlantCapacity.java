package es.deusto.sd.client.data;

public class PlantCapacity {
    private Long plantId;
    private Double currentCapacity; // Usamos Double para coincidir con el servidor

    public PlantCapacity() {}

    public PlantCapacity(Long plantId, Double currentCapacity) {
        this.plantId = plantId;
        this.currentCapacity = currentCapacity;
    }

    public Long getPlantId() { return plantId; }
    public void setPlantId(Long plantId) { this.plantId = plantId; }

    public Double getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(Double currentCapacity) { this.currentCapacity = currentCapacity; }
}