package es.deusto.sd.ecoembes.external;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import es.deusto.sd.ecoembes.dto.AssignmentDTO;
import es.deusto.sd.ecoembes.dto.PlantCapacityDTO;



public interface IServiceGateway {
	public List<PlantCapacityDTO> getCapacity(LocalDate date); 
    public PlantCapacityDTO getSinglePlantCapacity(Long plantId,LocalDate date); //only one recycling plant capacity
    public AssignmentDTO assignDumpsterToPlant(AssignmentDTO assignmentDTO); //for assigning a Dumpster to Plant


    
}

