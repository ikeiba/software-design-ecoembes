package es.deusto.sd.ecoembes.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;

@Repository
public interface DumpsterUsageRecordRepository extends JpaRepository<DumpsterUsageRecord, Long> {
    List<DumpsterUsageRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
