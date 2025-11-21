package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import es.deusto.sd.ecoembes.dto.AssignmentExternalNotificationDTO;

//Here we will implement de client socket

@Component("ContSocket Ltd.") //because it must be a singleton
@Lazy
public class ContSocketGateway implements IServiceGateway {

 	
	//API Server Host and Port NOT hard-coded: Defined in application.properties
    @Value("${gateway.cont.ip}")
    private String serverIP;

    @Value("${gateway.cont.port}")
    private int serverPort;
    
	public ContSocketGateway(){
    }

    @Override
    public double getCapacity(LocalDate date) {

        String datestr = date.toString();
		double capacity = 0.0; 

		try (Socket socket = new Socket(serverIP, serverPort);
			//Streams to send and receive information are created from the Socket
            DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			
			//we first send a prefix to the server saying the type of value we are sending. 
			out.writeUTF("String");
			out.flush();
			//Send request (one String) to the server
			out.writeUTF(datestr);
			System.out.println(" - Sending DATE to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + datestr + "'");
			
			capacity = in.readDouble();
         

			System.out.println(" - Getting PlantCapacityDTO from'" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + String.valueOf(capacity) + "'");

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
    public AssignmentExternalNotificationDTO assignDumpsterToPlant(AssignmentExternalNotificationDTO assignmentExternalNotificationDTO) {

		AssignmentExternalNotificationDTO assignmentExternalDTO_ret = null;

		try (var socket = new Socket(serverIP, serverPort);
			//Streams to send and receive information are created from the Socket
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
			try {
			//we first send a prefix to the server saying the type of value we are sending. 
			out.writeUTF("Object");
			out.flush();
				out.writeObject(assignmentExternalNotificationDTO);
			}catch (IOException e) {
			}

			System.out.println(" - Sending AssignmentDTO to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + assignmentExternalNotificationDTO.getToken() + "'");

			try {
				assignmentExternalDTO_ret = (AssignmentExternalNotificationDTO) in.readObject();
			} catch (ClassNotFoundException e) {
			}
			System.out.println(" - Getting AssignmentDTO from'" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + (assignmentExternalNotificationDTO.getToken() != null ? assignmentExternalNotificationDTO.getToken() : "null") + "'");

		} catch (UnknownHostException e) {
			System.err.println("# Trans. SocketClient: Socket error: " + e.getMessage());	
		} catch (EOFException e) {
			System.err.println("# Trans. SocketClient: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# Trans. SocketClient: IO error: " + e.getMessage());
		}

    
        return assignmentExternalDTO_ret;
		
    }






}

