package es.deusto.sd.plas.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.plas.model.Assignment;

public interface PlasAssignmentRepository extends JpaRepository<Assignment, Long> {
  Optional<Assignment> findByDate(LocalDate date);
}
