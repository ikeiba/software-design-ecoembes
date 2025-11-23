package es.deusto.sd.ecoembes.external;

import java.time.LocalDate;

import es.deusto.sd.ecoembes.dto.AssignmentExternalNotificationDTO;



public interface IServiceGateway {
	public double getCapacity(LocalDate date); //yo creo que solo hace falta este
    
    //in this case nDumpster will be 1, but we will let the option of passing as parameter the numberOfDumpster. Perhaps it will be useful in the future.
    public AssignmentExternalNotificationDTO assignDumpsterToPlant(AssignmentExternalNotificationDTO assignmentExternalNotificationDTO); //for assigning a Dumpster to Plant


    
}

