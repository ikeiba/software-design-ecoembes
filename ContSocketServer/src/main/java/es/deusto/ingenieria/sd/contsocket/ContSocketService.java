package es.deusto.ingenieria.sd.contsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.deusto.ingenieria.sd.contsocket.dto.AssignmentExternalNotificationDTO;

public class ContSocketService extends Thread {


    private final Socket tcpSocket;
	private static Map<LocalDate, Double> capacitymap;
    private static final double TONS_PER_CONTAINER = 0.00005; //0,5 kg each container
    private static final double MAX_DAILY_CAPACITY = 1000.0; //tons



    public ContSocketService(Socket socket, Map<LocalDate,Double> capacitymap) {
        this.tcpSocket = socket;
        ContSocketService.capacitymap = capacitymap;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // ✅ Añade esto
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ✅ Opcional

        try (DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());
            DataInputStream in = new DataInputStream(tcpSocket.getInputStream())) {

            String prefix = in.readUTF();

            if ("String".equals(prefix)) {
                String text = in.readUTF();
                LocalDate date = LocalDate.parse(text);
                double response = getCapacity(date);

                out.writeDouble(response);
                out.flush();
            } else if("JSON".equals(prefix)) {
                String jsonReceived = in.readUTF();
                AssignmentExternalNotificationDTO dto =
                        mapper.readValue(jsonReceived, AssignmentExternalNotificationDTO.class);                

                AssignmentExternalNotificationDTO result = assignDumpsterToPlant(dto);

                String jsonToSend = mapper.writeValueAsString(result);
                out.writeUTF(jsonToSend);
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Error en el servicio para cliente " + tcpSocket.getInetAddress());
            e.printStackTrace();
        } finally {
            try {
                if (!tcpSocket.isClosed()) {
                    tcpSocket.close();
                }
            } catch (IOException e) {
                // Ignorar
            }
        }
    }
    // -----------------------
    // Métodos internos
    // -----------------------


    //  public AssignmentExternalNotificationDTO(LocalDate date, int nDumpster, int nContainer) {

    private AssignmentExternalNotificationDTO assignDumpsterToPlant(AssignmentExternalNotificationDTO dto) {
        LocalDate date = dto.getDate();
        int nContainer = dto.getnContainer();
        double tons = TONS_PER_CONTAINER * nContainer;
        
        double day_capacity = capacitymap.getOrDefault(date, MAX_DAILY_CAPACITY);

        double new_day_capacity = day_capacity - tons;

        capacitymap.put(date,new_day_capacity);
        return dto;  //we return the original the dto to confirm that the assignment was succesfull
    }

    private double getCapacity(LocalDate date) {
            
        return capacitymap.getOrDefault(date, 1000.0);
          
    }



}
