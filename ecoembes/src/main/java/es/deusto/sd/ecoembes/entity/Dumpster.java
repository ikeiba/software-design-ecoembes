package es.deusto.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Dumpster {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // Unique identifier

  @Column(nullable = false)
  private String location; // Location (address)

  @Column(nullable = false)
  private String postalCode; // Postal code

  @Column(nullable = false)
  private double capacity; // Initial capacity

  // Current Status (simulated from sensor updates)
  @Column(nullable = false)
  private int estimatedContainers;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FillLevel fillLevel;

  // Historical data to simulate usage queries
  @OneToMany(mappedBy = "dumpster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<DumpsterUsageRecord> usageHistory;

  public Dumpster() {
    this.usageHistory = new ArrayList<>();
  }

  public Dumpster(String location, String postalCode, double capacity) {
    this.location = location;
    this.postalCode = postalCode;
    this.capacity = capacity;
    this.fillLevel = FillLevel.GREEN; // Default initial state
    this.estimatedContainers = 0;
    this.usageHistory = new ArrayList<>();
  }

  // Method to simulate a sensor update
  public void updateStatus(int newContainerCount, FillLevel newFillLevel) {
    this.estimatedContainers = newContainerCount;
    this.fillLevel = newFillLevel;
    // Add to history with back-reference
    var record = new DumpsterUsageRecord(java.time.LocalDate.now(), newContainerCount, newFillLevel);
    record.setDumpster(this);
    this.usageHistory.add(record);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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