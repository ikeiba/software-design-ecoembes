package es.deusto.sd.ecoembes.facade;

import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dto.AssignmentDTO;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.DumpsterStatusDTO;
import es.deusto.sd.ecoembes.dto.DumpsterUpdateDTO;
import es.deusto.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.sd.ecoembes.dto.NewDumpsterDTO;
import es.deusto.sd.ecoembes.dto.PlantCapacityDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.service.EcoembesService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations related to dumpsters and recycling plants")
public class EcoembesController {


    //! AQUI HAY QUE PONER VARIOS SERVICES PORQUE CADA UNO VA A GESTIONAR UNA PARTE DIFERENTE
    private final EcoembesService ecoembesService;

    public EcoembesController(EcoembesService ecoembesService) {
        this.ecoembesService = ecoembesService;
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
                newDumpsterDTO.getDumpsterId(),
                newDumpsterDTO.getLocation(),
                newDumpsterDTO.getPostalCode(),
                newDumpsterDTO.getInitialCapacity()
            );
            
            // Convert entity back to DTO
            DumpsterDTO responseDTO = new DumpsterDTO(
                dumpster.getId(),
                dumpster.getLocation(),
                dumpster.getCapacity()
            );
            
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
            @PathVariable("id") String dumpsterId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        try {
            // Get entity list from service
            List<DumpsterUsageRecord> usageRecords = ecoembesService.getDumpsterUsage(dumpsterId, startDate, endDate);

            // Convert entities to DTOs
            List<DumpsterUsageDTO> usageList = usageRecords.stream()
                .map(record -> new DumpsterUsageDTO(
                    record.getDate(),
                    record.getEstimatedContainers(),
                    record.getFillLevel()
                ))
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
                .map(dumpster -> {
                    var fillLevel = ecoembesService.getDumpsterFillLevelOnDate(dumpster, date);
                    return new DumpsterStatusDTO(
                        dumpster.getId(),
                        dumpster.getLocation(),
                        fillLevel
                    );
                })
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
                .map(plant -> {
                    double availableCapacity = ecoembesService.calculatePlantCapacity(plant.getPlantId(), date);
                    return new PlantCapacityDTO(plant.getPlantId(), availableCapacity);
                })
                .toList();

            if (capacities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(capacities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Assign dumpsters to recycling plants
    @Operation(summary = "Assign dumpsters to recycling plants", description = "Assigns a list of dumpsters to a specified recycling plant", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Asignación completada"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data"),
            @ApiResponse(responseCode = "404", description = "Not Found: Plant or dumpsters not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/plants/assign")
    public ResponseEntity<String> assignDumpstersToPlant(@RequestBody AssignmentDTO assignmentDTO) {
        try {
            // Convert DTO to entity parameters and call service
            ecoembesService.assignDumpstersToPlant(
                assignmentDTO.getPlantId(),
                assignmentDTO.getDumpsterIds()
            );
            return new ResponseEntity<>("Asignación completada", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
