package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Assignment {

    private String assignmentId;
    private LocalDate date;
    private String dumpsterId;
    private String employeeId;
    private String plantId;

    // Constructors
    public Assignment() {
    }

    public Assignment(String assignmentId, LocalDate date, String dumpsterId, String employeeId, String plantId) {
        this.assignmentId = assignmentId;
        this.date = date;
        this.dumpsterId = dumpsterId;
        this.employeeId = employeeId;
        this.plantId = plantId;
    }

    // Getters and Setters
    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDumpsterId() {
        return dumpsterId;
    }

    public void setDumpsterId(String dumpsterId) {
        this.dumpsterId = dumpsterId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(assignmentId, that.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId='" + assignmentId + '\'' +
                ", date=" + date +
                ", dumpsterId='" + dumpsterId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", plantId='" + plantId + '\'' +
                '}';
    }
}
