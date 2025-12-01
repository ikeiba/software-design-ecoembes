package es.deusto.sd.plas.dao;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.plas.Entities.Assignment;

public interface PlasAssignmentRepository extends JpaRepository<Assignment, Long> {
  Optional<Assignment> findByDate(LocalDate date);
}
