package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "dumpster_id", nullable = false)
    private Dumpster dumpster;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private RecyclingPlant plant;

    public Assignment() {
    }

    public Assignment(LocalDate date, Dumpster dumpster, Employee employee, RecyclingPlant plant) {
        this.date = date;
        this.dumpster = dumpster;
        this.employee = employee;
        this.plant = plant;
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

    public Dumpster getDumpster() {
        return dumpster;
    }

    public void setDumpster(Dumpster dumpster) {
        this.dumpster = dumpster;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RecyclingPlant getPlant() {
        return plant;
    }

    public void setPlant(RecyclingPlant plant) {
        this.plant = plant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", date=" + date +
                ", dumpster=" + (dumpster != null ? dumpster.getId() : null) +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", plant=" + (plant != null ? plant.getId() : null) +
                '}';
    }
}
