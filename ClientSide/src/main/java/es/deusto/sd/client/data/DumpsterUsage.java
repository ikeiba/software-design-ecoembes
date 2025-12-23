package es.deusto.sd.client.data;

import java.time.LocalDate;

public class DumpsterUsage {
    private LocalDate date;
    private Integer estimatedContainers;
    private String fillLevel;

    public DumpsterUsage() {}

    public DumpsterUsage(LocalDate date, Integer estimatedContainers, String fillLevel) {
        this.date = date;
        this.estimatedContainers = estimatedContainers;
        this.fillLevel = fillLevel;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getEstimatedContainers() { return estimatedContainers; }
    public void setEstimatedContainers(Integer estimatedContainers) { this.estimatedContainers = estimatedContainers; }

    public String getFillLevel() { return fillLevel; }
    public void setFillLevel(String fillLevel) { this.fillLevel = fillLevel; }
}