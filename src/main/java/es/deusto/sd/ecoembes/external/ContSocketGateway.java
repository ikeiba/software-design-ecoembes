package es.deusto.sd.ecoembes.external;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.deusto.sd.ecoembes.dto.AssignmentDTO;
import es.deusto.sd.ecoembes.dto.PlantCapacityDTO;

//Here we will implement de client socket

@Component //because it must be a singleton
public class ContSocketGateway implements IServiceGateway {

 	
	//API Server Host and Port NOT hard-coded: Defined in application.properties
    @Value("${gateway.cont.ip}")
    private String serverIP;

    @Value("${gateway.cont.port}")
    private int serverPort;
    public ContSocketGateway(){
    }



    @Override
    public List<PlantCapacityDTO> getCapacity(LocalDate date) {

        List<PlantCapacityDTO> response = new ArrayList();
		PlantCapacityDTO receivedPlantCapacityDto = null; 
        String datestr = date.toString();

		try (Socket socket = new Socket(serverIP, serverPort);
			//Streams to send and receive information are created from the Socket
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			
			//we first send a prefix to the server saying the type of value we are sending. 
			out.writeUTF("String");
			out.flush();
			//Send request (one String) to the server
			out.writeUTF(datestr);
			System.out.println(" - Sending DATE to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + datestr + "'");
			
			try {
				receivedPlantCapacityDto = (PlantCapacityDTO) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}           
			response.add(receivedPlantCapacityDto);

			System.out.println(" - Getting PlantCapacityDTO from'" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + String.valueOf(receivedPlantCapacityDto.getPlantId()) + "'");

		} catch (UnknownHostException e) {
			System.err.println("# Trans. SocketClient: Socket error: " + e.getMessage());	
		} catch (EOFException e) {
			System.err.println("# Trans. SocketClient: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# Trans. SocketClient: IO error: " + e.getMessage());
		}

    
        return response;
    }

  

    @Override
    public AssignmentDTO assignDumpsterToPlant(AssignmentDTO assignmentDTO) {

		AssignmentDTO assignmentDTO_ret = null;

		try (var socket = new Socket(serverIP, serverPort);
			//Streams to send and receive information are created from the Socket
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
			try {
			//we first send a prefix to the server saying the type of value we are sending. 
			out.writeUTF("Object");
			out.flush();
				out.writeObject(assignmentDTO);
			}catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println(" - Sending AssignmentDTO to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + assignmentDTO.getToken() + "'");

			try {
				assignmentDTO_ret = (AssignmentDTO) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println(" - Getting AssignmentDTO from'" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + (assignmentDTO_ret.getToken() != null ? assignmentDTO_ret.getToken() : "null") + "'");

		} catch (UnknownHostException e) {
			System.err.println("# Trans. SocketClient: Socket error: " + e.getMessage());	
		} catch (EOFException e) {
			System.err.println("# Trans. SocketClient: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# Trans. SocketClient: IO error: " + e.getMessage());
		}

    
        return assignmentDTO_ret;
		
    }




}

