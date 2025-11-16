/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevel;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class EcoembesService {

	// In-memory repositories (simulation)
	private final Map<Long, Dumpster> dumpsterRepository = new HashMap<>();
	private final Map<Long, RecyclingPlant> plantRepository = new HashMap<>();
	private final Map<Long, Employee> employeeRepository = new HashMap<>();
	private final Map<Long, Assignment> assignmentRepository = new HashMap<>();

	public EcoembesService() {
		// Seed some demo plants
		RecyclingPlant p1 = new RecyclingPlant("PlasSB Ltd.");
		RecyclingPlant p2 = new RecyclingPlant("ContSocket SA");
		p1.setId(1L);
		p2.setId(2L);
		plantRepository.put(p1.getId(), p1);
		plantRepository.put(p2.getId(), p2);
	}

	// -------------------------- Service methods (work only with entities) --------------------------

	/**
	 * Updates the status of a dumpster with sensor data
	 */
	public void updateDumpsterInfo(Long dumpsterId, int estimatedContainers, FillLevel fillLevel) {
		if (dumpsterId == null) {
			throw new IllegalArgumentException("Dumpster ID cannot be null");
		}
		if (estimatedContainers < 0) {
			throw new IllegalArgumentException("Estimated containers cannot be negative");
		}
		if (fillLevel == null) {
			throw new IllegalArgumentException("Fill level cannot be null");
		}

		Dumpster d = dumpsterRepository.get(dumpsterId);
		if (d == null) {
			throw new RuntimeException("Dumpster not found");
		}

		d.updateStatus(estimatedContainers, fillLevel);
	}

	/**
	 * Creates a new dumpster
	 */
	public Dumpster createDumpster(String location, String postalCode, double initialCapacity) {
		if (location == null || location.isBlank()) {
			throw new IllegalArgumentException("Location cannot be null or empty");
		}
		if (postalCode == null || postalCode.isBlank()) {
			throw new IllegalArgumentException("Postal code cannot be null or empty");
		}
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException("Initial capacity must be greater than zero");
		}

		Dumpster d = new Dumpster(location, postalCode, initialCapacity);
		// ID will be auto-generated when persisted to database
		// For now in-memory, generate a simple ID
		Long newId = (long) (dumpsterRepository.size() + 1);
		d.setId(newId);
		dumpsterRepository.put(d.getId(), d);
		return d;
	}

	/**
	 * Gets usage history for a dumpster within a date range
	 */
	public List<DumpsterUsageRecord> getDumpsterUsage(Long dumpsterId, LocalDate startDate, LocalDate endDate) {
		if (dumpsterId == null) {
			throw new IllegalArgumentException("Invalid dumpster ID");
		}
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("Start date and end date cannot be null");
		}
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date must be before or equal to end date");
		}

		Dumpster d = dumpsterRepository.get(dumpsterId);
		if (d == null) {
			throw new RuntimeException("Dumpster not found");
		}

		return d.getUsageHistory().stream()
				.filter(r -> !(r.getDate().isBefore(startDate) || r.getDate().isAfter(endDate)))
				.collect(Collectors.toList());
	}

	/**
	 * Gets all dumpsters in a specific postal code area with their status on a given date
	 */
	public List<Dumpster> getDumpstersByArea(String postalCode) {
		if (postalCode == null || postalCode.isBlank()) {
			throw new IllegalArgumentException("Postal code cannot be null or empty");
		}

		return dumpsterRepository.values().stream()
				.filter(d -> d.getPostalCode() != null && d.getPostalCode().equals(postalCode))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the fill level for a dumpster on a specific date
	 */
	public FillLevel getDumpsterFillLevelOnDate(Dumpster dumpster, LocalDate date) {
		if (dumpster == null) {
			throw new IllegalArgumentException("Dumpster cannot be null");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}

		DumpsterUsageRecord match = dumpster.getUsageHistory().stream()
				.filter(r -> r.getDate().equals(date))
				.findFirst().orElse(null);

		return (match != null) ? match.getFillLevel() : dumpster.getFillLevel();
	}

	/**
	 * Gets all recycling plants
	 */
	public List<RecyclingPlant> getAllPlants() {
		return new ArrayList<>(plantRepository.values());
	}

	/**
	 * Gets a single recycling plant by ID
	 */
	public RecyclingPlant getPlantById(Long plantId) {
		if (plantId == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}
		RecyclingPlant plant = plantRepository.get(plantId);
		if (plant == null) {
			throw new RuntimeException("Plant not found");
		}
		return plant;
	}

	/**
	 * Gets all dumpsters assigned to a plant on a specific date
	 */
	public List<Dumpster> getAssignedDumpsters(Long plantId, LocalDate date) {
		if (plantId == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}

		return assignmentRepository.values().stream()
				.filter(a -> a.getPlant().getId().equals(plantId) && a.getDate().equals(date))
				.map(Assignment::getDumpster)
				.filter(d -> d != null)
				.collect(Collectors.toList());
	}

	/**
	 * Calculates available capacity for a plant on a given date
	 */
	public double calculatePlantCapacity(Long plantId, LocalDate date) {
		if (plantId == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		if (!plantRepository.containsKey(plantId)) {
			throw new RuntimeException("Plant not found");
		}

		List<Dumpster> assignedDumpsters = getAssignedDumpsters(plantId, date);

		double totalCapacityTons = 0.0;
		double occupiedTons = 0.0;

		for (Dumpster d : assignedDumpsters) {
			if (d == null) {
				continue;
			}
			
			double capacityKg = d.getCapacity();
			totalCapacityTons += capacityKg / 1000.0; // kg -> tons

			// Use the usage record for the date if available, else current fill level
			DumpsterUsageRecord match = d.getUsageHistory().stream()
					.filter(r -> r.getDate().equals(date))
					.findFirst().orElse(null);

			FillLevel fillLevel = (match != null) ? match.getFillLevel() : d.getFillLevel();
			
			// Calculate occupied weight based on fill level percentage
			double fillPercentage = getFillLevelPercentage(fillLevel);
			double occupiedKg = capacityKg * fillPercentage;
			occupiedTons += occupiedKg / 1000.0;
		}

		return Math.max(0.0, totalCapacityTons - occupiedTons);
	}

	/**
	 * Helper method to convert FillLevel enum to percentage
	 */
	private double getFillLevelPercentage(FillLevel fillLevel) {
		switch (fillLevel) {
			case GREEN:
				return 0.33; // 0-33% full
			case ORANGE:
				return 0.66; // 33-66% full
			case RED:
				return 0.90; // 66-100% full (assume 90% average)
			default:
				return 0.0;
		}
	}

	/**
	 * Assigns a single dumpster to a recycling plant for a specific date
	 */
	public Assignment assignDumpsterToPlant(LocalDate date, Long dumpsterId, Long employeeId, Long plantId) {
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		if (dumpsterId == null) {
			throw new IllegalArgumentException("Dumpster ID cannot be null");
		}
		if (employeeId == null) {
			throw new IllegalArgumentException("Employee ID cannot be null");
		}
		if (plantId == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}

		Dumpster dumpster = dumpsterRepository.get(dumpsterId);
		Employee employee = employeeRepository.get(employeeId);
		RecyclingPlant plant = plantRepository.get(plantId);

		if (dumpster == null) {
			throw new RuntimeException("Dumpster not found: " + dumpsterId);
		}
		if (employee == null) {
			throw new RuntimeException("Employee not found: " + employeeId);
		}
		if (plant == null) {
			throw new RuntimeException("Plant not found: " + plantId);
		}

		// Check if this dumpster is already assigned on this date
		boolean alreadyAssigned = assignmentRepository.values().stream()
				.anyMatch(a -> a.getDumpster().getId().equals(dumpsterId) && a.getDate().equals(date));

		if (alreadyAssigned) {
			throw new IllegalArgumentException("Dumpster " + dumpsterId + " is already assigned for date " + date);
		}

		Assignment assignment = new Assignment(date, dumpster, employee, plant);
		// ID will be auto-generated when persisted to database
		// For now in-memory, generate a simple ID
		Long newId = (long) (assignmentRepository.size() + 1);
		assignment.setId(newId);
		assignmentRepository.put(newId, assignment);

		return assignment;
	}

	/**
	 * Gets all assignments for a specific date
	 */
	public List<Assignment> getAssignmentsByDate(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		return assignmentRepository.values().stream()
				.filter(a -> a.getDate().equals(date))
				.collect(Collectors.toList());
	}

	/**
	 * Gets all assignments for a specific plant
	 */
	public List<Assignment> getAssignmentsByPlant(Long plantId) {
		if (plantId == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}
		return assignmentRepository.values().stream()
				.filter(a -> a.getPlant() != null && a.getPlant().getId().equals(plantId))
				.collect(Collectors.toList());
	}

	/**
	 * Adds a dumpster to the repository (used by DataInitializer)
	 */
	public void addDumpster(Dumpster dumpster) {
		if (dumpster == null) {
			throw new IllegalArgumentException("Dumpster cannot be null");
		}
		if (dumpster.getId() == null) {
			throw new IllegalArgumentException("Dumpster ID cannot be null");
		}
		dumpsterRepository.putIfAbsent(dumpster.getId(), dumpster);
	}	

	/**
	 * Adds a recycling plant to the repository (used by DataInitializer)
	 */
	public void addPlant(RecyclingPlant plant) {
		if (plant == null) {
			throw new IllegalArgumentException("Plant cannot be null");
		}
		if (plant.getId() == null) {
			throw new IllegalArgumentException("Plant ID cannot be null");
		}
		plantRepository.putIfAbsent(plant.getId(), plant);
	}

	/**
	 * Adds an employee to the repository (used by DataInitializer)
	 */
	public void addEmployee(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException("Employee cannot be null");
		}
		if (employee.getId() == null) {
			throw new IllegalArgumentException("Employee ID cannot be null");
		}
		employeeRepository.putIfAbsent(employee.getId(), employee);
	}

}