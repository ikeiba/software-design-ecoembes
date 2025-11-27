package es.deusto.sd.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.ecoembes.dto.AssignmentDTO;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.DumpsterStatusDTO;
import es.deusto.sd.ecoembes.dto.DumpsterUpdateDTO;
import es.deusto.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.sd.ecoembes.dto.NewDumpsterDTO;
import es.deusto.sd.ecoembes.dto.PlantCapacityDTO;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.service.AuthService;
import es.deusto.sd.ecoembes.service.EcoembesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations related to dumpsters and recycling plants")
public class EcoembesController {

    private static final Logger logger = LoggerFactory.getLogger(EcoembesController.class);

    //! AQUI HAY QUE PONER VARIOS SERVICES PORQUE CADA UNO VA A GESTIONAR UNA PARTE DIFERENTE
    private final EcoembesService ecoembesService;
    private final AuthService authService;
    public EcoembesController(EcoembesService ecoembesService, AuthService authService) {
        this.ecoembesService = ecoembesService;
        this.authService = authService;
    }

    // PUT: Update info (Sensor)
    @Operation(summary = "Update dumpster info", description = "Updates fill level and container count from sensors", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Actualización recibida"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/dumpsters/update")
    public ResponseEntity<String> updateDumpsterInfo(@RequestBody DumpsterUpdateDTO updateDTO) {
        try {
            // Convert DTO to entity parameters and call service
            ecoembesService.updateDumpsterInfo(
                updateDTO.getDumpsterId(),
                updateDTO.getEstimatedContainers(),
                updateDTO.getFillLevel()
            );
            return new ResponseEntity<>("Actualización recibida", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Create a new dumpster
    @Operation(summary = "Create a new dumpster", description = "Creates a new dumpster with its initial capacity and location", responses = {
            @ApiResponse(responseCode = "201", description = "Created: Dumpster created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestBody NewDumpsterDTO newDumpsterDTO) {
        try {
            // Convert DTO to entity parameters and call service
            Dumpster dumpster = ecoembesService.createDumpster(
                newDumpsterDTO.getLocation(),
                newDumpsterDTO.getPostalCode(),
                newDumpsterDTO.getInitialCapacity()
            );
            
            // Convert entity back to DTO
            DumpsterDTO responseDTO = toDumpsterDTO(dumpster);
            
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Query dumpster usage
    @Operation(summary = "Query dumpster usage", description = "Returns the list of usage records of a dumpster between two dates", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Usage records retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No usage records found"),
            @ApiResponse(responseCode = "404", description = "Not Found: Dumpster not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dumpsters/{id}/usage")
    public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(
            @PathVariable("id") Long dumpsterId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        try {
            // Get entity list from service
            List<DumpsterUsageRecord> usageRecords = ecoembesService.getDumpsterUsage(dumpsterId, startDate, endDate);

            // Convert entities to DTOs
            List<DumpsterUsageDTO> usageList = usageRecords.stream()
                .map(this::toDumpsterUsageDTO)
                .toList();

            if (usageList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usageList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Check dumpster status by area
    @Operation(summary = "Check dumpster status by area", description = "Returns the list of dumpsters and their status for a given postal code and date", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Status retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No dumpsters found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dumpsters/status")
    public ResponseEntity<List<DumpsterStatusDTO>> getDumpsterStatusByArea(
            @RequestParam("postalCode") String postalCode,
            @RequestParam("date") LocalDate date) {
        try {
            // Get entities from service
            List<Dumpster> dumpsters = ecoembesService.getDumpstersByArea(postalCode);

            // Convert entities to DTOs with status for the specific date
            List<DumpsterStatusDTO> statusList = dumpsters.stream()
                .map(dumpster -> toDumpsterStatusDTO(dumpster, date))
                .toList();

            if (statusList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(statusList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Check recycling plant capacity
    @Operation(summary = "Check recycling plant capacity", description = "Returns the capacity of all recycling plants for a given date", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Capacity retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No data found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/plants/capacity")
    public ResponseEntity<List<PlantCapacityDTO>> getRecyclingPlantCapacity(
            @RequestParam("date") LocalDate date) {
        try {
            // Get all plants from service
            List<RecyclingPlant> plants = ecoembesService.getAllPlants();
            
            // Convert entities to DTOs with calculated capacity
            List<PlantCapacityDTO> capacities = plants.stream()
                .map(plant -> toPlantCapacityDTO(plant, date))
                .toList();

            if (capacities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(capacities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Check single recycling plant capacity
    @Operation(summary = "Check single recycling plant capacity", description = "Returns the capacity of a specific recycling plant for a given date", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Capacity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found: Plant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/plants/{plantId}/capacity")
    public ResponseEntity<PlantCapacityDTO> getSinglePlantCapacity(
            @PathVariable("plantId") Long plantId,
            @RequestParam("date")
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        try {
            // Get plant from service
            RecyclingPlant plant = ecoembesService.getPlantById(plantId);

            // Convert entity to DTO with calculated capacity
            PlantCapacityDTO capacity = toPlantCapacityDTO(plant, date);

            return new ResponseEntity<>(capacity, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Assign a dumpster to a recycling plant
    @Operation(summary = "Assign a dumpster to a recycling plant", description = "Assigns a single dumpster to a specified recycling plant for a specific date", responses = {
            @ApiResponse(responseCode = "201", description = "Created: Asignación completada"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing token"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data or dumpster already assigned"),
            @ApiResponse(responseCode = "404", description = "Not Found: Plant, dumpster, or employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/assignments")
    public ResponseEntity<AssignmentDTO> assignDumpsterToPlant(
            @RequestBody AssignmentDTO assignmentDTO) {
        try {
            logger.info("Assignment request received - DTO: date={}, dumpsterId={}, plantId={}", 
                    assignmentDTO.getDate(), assignmentDTO.getDumpsterId(), assignmentDTO.getPlantId());
            
            var employee = authService.getUserByToken(assignmentDTO.getToken());
            if (employee == null) {
                logger.warn("Invalid token provided");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            logger.info("Employee authenticated: id={}, name={}", employee.getId(), employee.getName());
            
            // Convert DTO to entity parameters and call service
            es.deusto.sd.ecoembes.entity.Assignment assignment = ecoembesService.assignDumpsterToPlant(
                assignmentDTO.getDate(),
                assignmentDTO.getDumpsterId(),
                employee.getId(),
                assignmentDTO.getPlantId()
            );
            
            // Convert entity back to DTO
            AssignmentDTO responseDTO = toAssignmentDTO(assignment);
            responseDTO.setToken(assignmentDTO.getToken());
            
            
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Bad request in assignment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            // Distingue entre NOT_FOUND y errores de gateway
            if (e.getMessage().contains("not found")) {
                logger.error("Resource not found in assignment: {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                logger.error("Gateway error in assignment: {}", e.getMessage(), e);
                // Log token vs expected information to help debugging 500 errors
                try {
                    String receivedToken = assignmentDTO != null ? assignmentDTO.getToken() : null;
                    var tokenUser = receivedToken != null ? authService.getUserByToken(receivedToken) : null;
                    if (tokenUser != null) {
                        logger.error("Gateway error in assignment: {}. Received token='{}' resolves to employee id={}, name={}",
                                e.getMessage(), receivedToken, tokenUser.getId(), tokenUser.getName(), e);
                    } else {
                        logger.error("Gateway error in assignment: {}. Received token='{}' does NOT resolve to a user (null). Payload: date={}, dumpsterId={}, plantId={}",
                                e.getMessage(), receivedToken,
                                assignmentDTO != null ? assignmentDTO.getDate() : null,
                                assignmentDTO != null ? assignmentDTO.getDumpsterId() : null,
                                assignmentDTO != null ? assignmentDTO.getPlantId() : null, e);
                    }
                } catch (Exception logEx) {
                    logger.error("Error while logging debug token info", logEx);
                }
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Internal error in assignment", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== DTO Conversion Methods =====

    private DumpsterDTO toDumpsterDTO(Dumpster dumpster) {
        return new DumpsterDTO(
            dumpster.getId(),
            dumpster.getLocation(),
            dumpster.getCapacity()
        );
    }

    private DumpsterUsageDTO toDumpsterUsageDTO(DumpsterUsageRecord record) {
        return new DumpsterUsageDTO(
            record.getDate(),
            record.getEstimatedContainers(),
            record.getFillLevel()
        );
    }

    private DumpsterStatusDTO toDumpsterStatusDTO(Dumpster dumpster, LocalDate date) {
        var fillLevel = ecoembesService.getDumpsterFillLevelOnDate(dumpster, date);
        return new DumpsterStatusDTO(
            dumpster.getId(),
            dumpster.getLocation(),
            fillLevel
        );
    }

    private PlantCapacityDTO toPlantCapacityDTO(RecyclingPlant plant, LocalDate date) {
        double availableCapacity = ecoembesService.calculatePlantCapacity(plant.getId(), date);
        return new PlantCapacityDTO(plant.getId(), availableCapacity);
    }

    private AssignmentDTO toAssignmentDTO(Assignment assignment) {
        return new AssignmentDTO(
            assignment.getDate(),
            assignment.getDumpster() != null ? assignment.getDumpster().getId() : null,
            assignment.getEmployee() != null ? assignment.getEmployee().getId() : null,
            assignment.getPlant() != null ? assignment.getPlant().getId() : null
        );
    }
}
