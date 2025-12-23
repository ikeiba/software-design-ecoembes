package es.deusto.sd.client.proxies;

import java.time.LocalDate;
import java.util.List;
import es.deusto.sd.client.data.*;

public interface IEcoembesServiceProxy {
    
    // Autenticación
    String login(Login credentials);
    void logout(String token);

    // Contenedores
    Dumpster createDumpster(NewDumpster newDumpster);
    
    // AHORA recibe la fecha explícitamente
    List<DumpsterStatus> getDumpsterStatus(String postalCode, LocalDate date);

    // Plantas
    // AHORA recibe la fecha explícitamente
    List<PlantCapacity> getPlantsCapacity(LocalDate date);

    // Asignación
    Assignment assignDumpster(Assignment assignment);
}