package es.deusto.sd.plas.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.plas.dto.AssignmentExternalNotificationDTO;

@RestController
public class PlasSBService {

  // Simple in-memory store of assignments (date -> token)
  private final Map<LocalDate, String> assignments = new HashMap<>();

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
    // simple acceptance: always accept and generate a token
    String token = "PLASSB-TOKEN-" + System.currentTimeMillis();
    assignments.put(dto.getDate(), token);
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
