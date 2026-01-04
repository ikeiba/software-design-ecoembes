package es.deusto.sd.plas.Facade;


import java.util.List;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.plas.dto.AssignmentExternalNotificationDTO;
import es.deusto.sd.plas.Entities.Assignment;
import es.deusto.sd.plas.dao.PlasAssignmentRepository;

@RestController
public class PlasSBController {

  private static final double TONS_PER_CONTAINER = 0.00005; //0,5 kg each container
  private static final double MAX_DAILY_CAPACITY = 1000.0; //tons

  private final PlasAssignmentRepository assignmentRepository;

  public PlasSBController(PlasAssignmentRepository assignmentRepository) {
    this.assignmentRepository = assignmentRepository;
  }

  @GetMapping(path = "/capacity", produces = MediaType.APPLICATION_JSON_VALUE)
  public double getCapacity(@RequestParam("date") String dateStr) {
    LocalDate date = LocalDate.parse(dateStr);
    double capacity = computeCapacity(date);
    System.out.println("PlasSB (HTTP): capacity request for " + dateStr + " -> " + capacity);
    return Math.floor(capacity * 100.0) / 100.0;
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
    double capacity = MAX_DAILY_CAPACITY;
    List<Assignment> assignments = assignmentRepository.findByDate(date);
    for (Assignment assignment : assignments) {
      capacity = capacity - (assignment.getnContainer()*TONS_PER_CONTAINER);
    }
    return Math.max(0.0, capacity);
  }

}
