package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;

public class Dumpster {

  private String id; // Unique identifier [cite: 73]
  private String location; // Location (address) [cite: 73]
  private double capacity; // Initial capacity [cite: 88]

  // Current Status (simulated from sensor updates)
  private int estimatedContainers; // [cite: 74]
  private FillLevel fillLevel; // [cite: 75]

  // Historical data to simulate usage queries
  private List<DumpsterUsageRecord> usageHistory;

  // Constructors
  public Dumpster() {
    this.usageHistory = new ArrayList<>();
  }

  public Dumpster(String id, String location, double capacity) {
    this.id = id;
    this.location = location;
    this.capacity = capacity;
    this.fillLevel = FillLevel.GREEN; // Default initial state
    this.estimatedContainers = 0;
    this.usageHistory = new ArrayList<>();
  }

  // Method to simulate a sensor update
  public void updateStatus(int newContainerCount, FillLevel newFillLevel) {
    this.estimatedContainers = newContainerCount;
    this.fillLevel = newFillLevel;
    // Add to history
    this.usageHistory.add(new DumpsterUsageRecord(java.time.LocalDate.now(), newContainerCount, newFillLevel));
  }

  // Getters and Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public List<DumpsterUsageRecord> getUsageHistory() {
    return usageHistory;
  }

  public void setUsageHistory(List<DumpsterUsageRecord> usageHistory) {
    this.usageHistory = usageHistory;
  }
}