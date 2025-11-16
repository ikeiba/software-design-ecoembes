package es.deusto.sd.ecoembes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.Dumpster;

@Repository
public interface DumpsterRepository extends JpaRepository<Dumpster, Long> {
    List<Dumpster> findByPostalCode(String postalCode);
}
