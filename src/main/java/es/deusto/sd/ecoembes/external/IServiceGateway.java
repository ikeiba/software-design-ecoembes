package es.deusto.sd.ecoembes.external;

import java.time.LocalDate;

import es.deusto.sd.ecoembes.dto.AssignmentExternalDTO;



public interface IServiceGateway {
	public double getCapacity(LocalDate date); //yo creo que solo hace falta este 
    public AssignmentExternalDTO assignDumpsterToPlant(AssignmentExternalDTO assignmentExternalDTO); //for assigning a Dumpster to Plant


    
}

