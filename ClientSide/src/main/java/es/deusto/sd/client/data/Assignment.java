package es.deusto.sd.client.data;

import java.time.LocalDate;

public class Assignment {
    private LocalDate date;
    private Long dumpsterId;
    private Long employeeId; // Aunque el token autentica, el DTO del servidor lo tiene. 
                             // Nota: El controller del servidor usa el token para sacar el employee,
                             // pero el objeto de respuesta podría devolverlo. Lo incluimos por si acaso.
    private Long plantId;
    private String token; // Necesario para la autenticación en la asignación según tu Controller

    public Assignment() {}

    public Assignment(LocalDate date, Long dumpsterId, Long plantId, String token) {
        this.date = date;
        this.dumpsterId = dumpsterId;
        this.plantId = plantId;
        this.token = token;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getDumpsterId() { return dumpsterId; }
    public void setDumpsterId(Long dumpsterId) { this.dumpsterId = dumpsterId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getPlantId() { return plantId; }
    public void setPlantId(Long plantId) { this.plantId = plantId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}