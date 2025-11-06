package es.deusto.sd.ecoembes.dto;

import java.util.List;

public class AssignmentDTO {
  private List<String> dumpsterIds;
  private String plantId;

  // Constructors
  public AssignmentDTO() {
  }

  public AssignmentDTO(List<String> dumpsterIds, String plantId) {
    this.dumpsterIds = dumpsterIds;
    this.plantId = plantId;
  }

  // Getters and Setters
  public List<String> getDumpsterIds() {
    return dumpsterIds;
  }

  public void setDumpsterIds(List<String> dumpsterIds) {
    this.dumpsterIds = dumpsterIds;
  }

  public String getPlantId() {
    return plantId;
  }

  public void setPlantId(String plantId) {
    this.plantId = plantId;
  }
}