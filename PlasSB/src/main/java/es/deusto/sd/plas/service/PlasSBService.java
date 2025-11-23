package es.deusto.sd.plas.service;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.plas.dto.AssignmentExternalNotificationDTO;
import es.deusto.sd.plas.model.Assignment;
import es.deusto.sd.plas.repository.PlasAssignmentRepository;

@RestController
public class PlasSBService {

  private final PlasAssignmentRepository assignmentRepository;

  public PlasSBService(PlasAssignmentRepository assignmentRepository) {
    this.assignmentRepository = assignmentRepository;
  }

  @GetMapping(path = "/capacity", produces = MediaType.APPLICATION_JSON_VALUE)
  public double getCapacity(@RequestParam("date") String dateStr) {
    LocalDate date = LocalDate.parse(dateStr);
    double capacity = computeCapacity(date);
    System.out.println("PlasSB (HTTP): capacity request for " + dateStr + " -> " + capacity);
    return capacity;
  }

  @PostMapping(path = "/assign", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public AssignmentExternalNotificationDTO assign(@RequestBody AssignmentExternalNotificationDTO dto) {
    System.out.println("PlasSB (HTTP): received assign request for " + dto.getDate());
    String token = "PLASSB-TOKEN-" + System.currentTimeMillis();

    Assignment entity = new Assignment(dto.getDate(), dto.getnDumpster(), dto.getnContainer(), token);
    assignmentRepository.save(entity);

    AssignmentExternalNotificationDTO ret = new AssignmentExternalNotificationDTO(dto.getDate(), dto.getnDumpster(),
        dto.getnContainer());
    ret.setToken(token);
    return ret;
  }

  private double computeCapacity(LocalDate date) {
    double base = 1000.0;
    double dec = date.getDayOfMonth() * 10.0;
    return Math.max(0.0, base - dec);
  }

}
