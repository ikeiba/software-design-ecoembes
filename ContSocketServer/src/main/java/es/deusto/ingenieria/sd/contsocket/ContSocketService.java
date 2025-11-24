package es.deusto.ingenieria.sd.contsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Map;

import es.deusto.ingenieria.sd.contsocket.dto.AssignmentExternalNotificationDTO;

public class ContSocketService extends Thread {

    private final Socket tcpSocket;
    private static Map<LocalDate, Double> capacitymap;
    private static final double TONS_PER_CONTAINER = 0.00005; // 0,5 kg each container
    private static final double MAX_DAILY_CAPACITY = 1000.0; // tons

    public ContSocketService(Socket socket, Map<LocalDate, Double> capacitymap) {
        this.tcpSocket = socket;
        ContSocketService.capacitymap = capacitymap;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(tcpSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(tcpSocket.getInputStream())) {

            // El cliente SIEMPRE manda primero un UTF indicando el tipo
            String command = in.readUTF();

            switch (command) {

                case "Object" -> {
                    AssignmentExternalNotificationDTO dto = (AssignmentExternalNotificationDTO) in.readObject();

                    AssignmentExternalNotificationDTO result = assignDumpsterToPlant(dto);

                    // Respuesta
                    out.writeObject(result);
                    out.flush();
                }

                case "String" -> {
                    String text = in.readUTF();

                    // Solo date
                    LocalDate date = LocalDate.parse(text);

                    double response = getCapacity(date);

                    out.writeObject(response);
                    out.flush();
                }

                default -> System.out.println("Unknown command: " + command);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el servicio para cliente " + tcpSocket.getInetAddress());
        } finally {
            // Aseguramos el cierre del socket al final de la comunicación
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

    // public AssignmentExternalNotificationDTO(LocalDate date, int nDumpster, int
    // nContainer) {

    private AssignmentExternalNotificationDTO assignDumpsterToPlant(AssignmentExternalNotificationDTO dto) {
        LocalDate date = dto.getDate();
        int nContainer = dto.getnContainer();
        double tons = TONS_PER_CONTAINER * nContainer;

        double day_capacity = capacitymap.getOrDefault(date, MAX_DAILY_CAPACITY);

        double new_day_capacity = day_capacity - tons;

        if (new_day_capacity <= 0) {
            throw new RuntimeException("Not enough capacity for assigning that dumpster");
        }

        capacitymap.put(date, new_day_capacity);
        return dto;
    }

    private double getCapacity(LocalDate date) {

        return capacitymap.get(date);
    }

}
