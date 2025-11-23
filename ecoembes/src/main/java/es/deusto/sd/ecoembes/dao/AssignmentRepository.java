package es.deusto.sd.ecoembes.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByDate(LocalDate date);
    List<Assignment> findByPlant(RecyclingPlant plant);
    List<Assignment> findByPlantAndDate(RecyclingPlant plant, LocalDate date);
    List<Assignment> findByDumpsterAndDate(Dumpster dumpster, LocalDate date);
}
