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

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EcoembesService {

	private final AuthService authService;

	// In-memory repositories (simulation)
	private final Map<String, Dumpster> dumpsterRepository = new HashMap<>();
	private final Map<String, RecyclingPlant> plantRepository = new HashMap<>();
	// plantId -> list of dumpsterIds
	private final Map<String, List<String>> plantAssignments = new HashMap<>();

	@Autowired
	public EcoembesService(AuthService authService) {
		this.authService = authService;

		// Seed some demo plants
		RecyclingPlant p1 = new RecyclingPlant("PlasSB", "PlasSB Ltd.");
		RecyclingPlant p2 = new RecyclingPlant("ContSocket", "ContSocket SA");
		plantRepository.put(p1.getPlantId(), p1);
		plantRepository.put(p2.getPlantId(), p2);
		plantAssignments.put(p1.getPlantId(), new ArrayList<>());
		plantAssignments.put(p2.getPlantId(), new ArrayList<>());
	}

	// -------------------------- Token-protected API (requested) --------------------------

	public DumpsterDTO registerNewDumpster(String token, NewDumpsterDTO dto) {
		// Validate token
		if (token == null || authService.getUserByToken(token) == null) {
			throw new IllegalArgumentException("Invalid or missing token");
		}

		if (dto == null || dto.getDumpsterId() == null || dto.getDumpsterId().isBlank()) {
			throw new IllegalArgumentException("Invalid dumpster data");
		}

		if (dumpsterRepository.containsKey(dto.getDumpsterId())) {
			throw new IllegalArgumentException("Dumpster with this id already exists");
		}

		Dumpster d = new Dumpster(dto.getDumpsterId(), dto.getLocation(), dto.getInitialCapacity());
		dumpsterRepository.put(d.getId(), d);

		return new DumpsterDTO(d.getId(), d.getLocation(), d.getCapacity());
	}

	public List<DumpsterStatusDTO> getDumpsterStatusByArea(String token, String postalCode, LocalDate date) {
		if (token == null || authService.getUserByToken(token) == null) {
			throw new IllegalArgumentException("Invalid or missing token");
		}

		// Simple location filtering: check if dumpster.location contains postalCode
		return dumpsterRepository.values().stream()
				.filter(d -> d.getLocation() != null && d.getLocation().contains(postalCode))
				.map(d -> {
					// Look for usage record on the requested date
					DumpsterUsageRecord match = d.getUsageHistory().stream()
							.filter(r -> r.getDate().equals(date))
							.findFirst().orElse(null);

					if (match != null) {
						return new DumpsterStatusDTO(d.getId(), d.getLocation(), match.getFillLevel());
					} else {
						return new DumpsterStatusDTO(d.getId(), d.getLocation(), d.getFillLevel());
					}
				}).collect(Collectors.toList());
	}

	public List<PlantCapacityDTO> getPlantCapacities(String token, LocalDate date) {
		if (token == null || authService.getUserByToken(token) == null) {
			throw new IllegalArgumentException("Invalid or missing token");
		}

		List<PlantCapacityDTO> result = new ArrayList<>();

		// For simplicity: availableCapacityTons = total assigned dumpster capacities (kg->tons) - occupiedTons
		// occupiedTons approximated as sum(estimatedContainers * 0.05)
		for (Map.Entry<String, RecyclingPlant> entry : plantRepository.entrySet()) {
			String plantId = entry.getKey();
			List<String> assigned = plantAssignments.getOrDefault(plantId, new ArrayList<>());

			double totalCapacityTons = 0.0;
			double occupiedTons = 0.0;

			for (String did : assigned) {
				Dumpster d = dumpsterRepository.get(did);
				if (d == null) continue;
				totalCapacityTons += d.getCapacity() / 1000.0; // assume capacity in kg -> tons

				// Use the usage record for the date if available, else current estimate
				DumpsterUsageRecord match = d.getUsageHistory().stream()
						.filter(r -> r.getDate().equals(date))
						.findFirst().orElse(null);

				int containers = (match != null) ? match.getEstimatedContainers() : d.getEstimatedContainers();
				occupiedTons += containers * 0.05; // heuristic: 0.05 tons per container
			}

			double available = Math.max(0.0, totalCapacityTons - occupiedTons);
			result.add(new PlantCapacityDTO(plantId, available));
		}

		return result;
	}

	public void assignDumpstersToPlant(String token, AssignmentDTO assignment) {
		if (token == null || authService.getUserByToken(token) == null) {
			throw new IllegalArgumentException("Invalid or missing token");
		}

		if (assignment == null || assignment.getPlantId() == null) {
			throw new IllegalArgumentException("Invalid assignment");
		}

		String plantId = assignment.getPlantId();
		if (!plantRepository.containsKey(plantId)) {
			throw new RuntimeException("Plant not found");
		}

		List<String> ids = plantAssignments.computeIfAbsent(plantId, k -> new ArrayList<>());

		for (String did : assignment.getDumpsterIds()) {
			if (!dumpsterRepository.containsKey(did)) {
				throw new RuntimeException("Dumpster not found: " + did);
			}
			if (!ids.contains(did)) ids.add(did);
		}
	}

	public void receiveSensorUpdate(DumpsterUpdateDTO update) {
		if (update == null || update.getDumpsterId() == null) {
			throw new IllegalArgumentException("Invalid update");
		}

		Dumpster d = dumpsterRepository.get(update.getDumpsterId());
		if (d == null) {
			throw new RuntimeException("Dumpster not found");
		}

		d.updateStatus(update.getEstimatedContainers(), update.getFillLevel());
	}

	// -------------------------- Backwards-compatible methods used by controller --------------------------

	public void updateDumpsterInfo(DumpsterUpdateDTO updateDTO) {
		// Controller version that doesn't use token: accept updates without auth
		receiveSensorUpdate(updateDTO);
	}

	public DumpsterDTO createDumpster(NewDumpsterDTO newDumpsterDTO) {
		// Controller version without token: create dumpster without auth
		if (newDumpsterDTO == null) throw new IllegalArgumentException("Invalid dumpster data");
		if (dumpsterRepository.containsKey(newDumpsterDTO.getDumpsterId()))
			throw new IllegalArgumentException("Dumpster with this id already exists");

		Dumpster d = new Dumpster(newDumpsterDTO.getDumpsterId(), newDumpsterDTO.getLocation(), newDumpsterDTO.getInitialCapacity());
		dumpsterRepository.put(d.getId(), d);
		return new DumpsterDTO(d.getId(), d.getLocation(), d.getCapacity());
	}

	public List<DumpsterUsageDTO> getDumpsterUsage(String dumpsterId, LocalDate startDate, LocalDate endDate) {
		Dumpster d = dumpsterRepository.get(dumpsterId);
		if (d == null) throw new RuntimeException("Dumpster not found");

		return d.getUsageHistory().stream()
				.filter(r -> !(r.getDate().isBefore(startDate) || r.getDate().isAfter(endDate)))
				.map(r -> new DumpsterUsageDTO(r.getDate(), r.getEstimatedContainers(), r.getFillLevel()))
				.collect(Collectors.toList());
	}

	public List<DumpsterStatusDTO> getDumpsterStatusByArea(String postalCode, LocalDate date) {
		// Controller-facing method without token
		return dumpsterRepository.values().stream()
				.filter(d -> d.getLocation() != null && d.getLocation().contains(postalCode))
				.map(d -> {
					DumpsterUsageRecord match = d.getUsageHistory().stream()
							.filter(r -> r.getDate().equals(date))
							.findFirst().orElse(null);
					if (match != null) {
						return new DumpsterStatusDTO(d.getId(), d.getLocation(), match.getFillLevel());
					} else {
						return new DumpsterStatusDTO(d.getId(), d.getLocation(), d.getFillLevel());
					}
				}).collect(Collectors.toList());
	}

	public List<PlantCapacityDTO> getRecyclingPlantCapacity(LocalDate date) {
		// Controller-facing method without token
		return getPlantCapacities(null, date).stream().collect(Collectors.toList());
	}

	public void assignDumpstersToPlant(AssignmentDTO assignmentDTO) {
		// Controller-facing method without token
		if (assignmentDTO == null) throw new IllegalArgumentException("Invalid assignment");
		String plantId = assignmentDTO.getPlantId();
		if (!plantRepository.containsKey(plantId)) throw new RuntimeException("Plant not found");

		List<String> ids = plantAssignments.computeIfAbsent(plantId, k -> new ArrayList<>());
		for (String did : assignmentDTO.getDumpsterIds()) {
			if (!dumpsterRepository.containsKey(did)) throw new RuntimeException("Dumpster not found: " + did);
			if (!ids.contains(did)) ids.add(did);
		}
	}

	// Crea el metodo ecoembesService.addDumpster utilizado en DataInitializer
	public void addDumpster(Dumpster dumpster) {
		if (dumpster != null && dumpster.getId() != null) {
			dumpsterRepository.putIfAbsent(dumpster.getId(), dumpster);
		}
	}	

	// Crea el metodo ecoembesService.addPlant utilizado en DataInitializer
	public void addPlant(RecyclingPlant plant) {
		if (plant != null && plant.getPlantId() != null) {
			plantRepository.putIfAbsent(plant.getPlantId(), plant);
			plantAssignments.putIfAbsent(plant.getPlantId(), new ArrayList<>());
		}
	}

}