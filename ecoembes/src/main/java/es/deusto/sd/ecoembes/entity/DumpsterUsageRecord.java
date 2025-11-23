package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DumpsterUsageRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private int estimatedContainers;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FillLevel fillLevel;

  @ManyToOne
  @JoinColumn(name = "dumpster_id", nullable = false)
  private Dumpster dumpster;

  public DumpsterUsageRecord() {
  }

  public DumpsterUsageRecord(LocalDate date, int estimatedContainers, FillLevel fillLevel) {
    this.date = date;
    this.estimatedContainers = estimatedContainers;
    this.fillLevel = fillLevel;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
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

  public Dumpster getDumpster() {
    return dumpster;
  }

  public void setDumpster(Dumpster dumpster) {
    this.dumpster = dumpster;
  }
}