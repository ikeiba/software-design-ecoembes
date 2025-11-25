package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.deusto.sd.ecoembes.dto.AssignmentExternalNotificationDTO;



//Here we will implement de client socket

@Component("ContSocket Ltd.") // because it must be a singleton
@Lazy
public class ContSocketGateway implements IServiceGateway {
    private static final Logger logger = LoggerFactory.getLogger(ContSocketGateway.class);

	// API Server Host and Port NOT hard-coded: Defined in application.properties
	@Value("${gateway.cont.ip}")
	private String serverIP;

	@Value("${gateway.cont.port}")
	private int serverPort;

	public ContSocketGateway() {
	}

	@Override
	public double getCapacity(LocalDate date) {

		String datestr = date.toString();
		double capacity = 0.0;

		try (Socket socket = new Socket(serverIP, serverPort);
				// Streams to send and receive information are created from the Socket
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

			// we first send a prefix to the server saying the type of value we are sending.
			out.writeUTF("String");
			out.flush();
			// Send request (one String) to the server
			out.writeUTF(datestr);
			System.out.println(" - Sending DATE to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort()
					+ "' -> '" + datestr + "'");

			capacity = in.readDouble();

			System.out.println(" - Getting PlantCapacityDTO from'" + socket.getInetAddress().getHostAddress() + ":"
					+ socket.getPort() + "' -> '" + String.valueOf(capacity) + "'");

		} catch (UnknownHostException e) {
			System.err.println("# Trans. SocketClient: Socket error: " + e.getMessage());
		} catch (EOFException e) {
			System.err.println("# Trans. SocketClient: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# Trans. SocketClient: IO error: " + e.getMessage());
		}
		return capacity;
	}
	@Override
	public AssignmentExternalNotificationDTO assignDumpsterToPlant(
		AssignmentExternalNotificationDTO assignmentExternalNotificationDTO) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // ✅ Añade esto
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ✅ Opcional: para formato ISO

		try (Socket socket = new Socket(serverIP, serverPort);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream())) {

			// Convert DTO to JSON
			String jsonToSend = mapper.writeValueAsString(assignmentExternalNotificationDTO);
			
			logger.info("Enviando prefijo JSON");
			out.writeUTF("JSON");
			out.flush();
			
			logger.info("Enviando JSON");
			out.writeUTF(jsonToSend);
			out.flush();
			logger.info("JSON enviado");

			logger.info("Esperando respuesta");
			String jsonResponse = in.readUTF();
			logger.info("Respuesta recibida");

			AssignmentExternalNotificationDTO response_dto =
					mapper.readValue(jsonResponse, AssignmentExternalNotificationDTO.class);

			return response_dto;

		} catch (Exception e) {
			throw new RuntimeException("Error sending assignment to server2", e);
		}
	}


}
