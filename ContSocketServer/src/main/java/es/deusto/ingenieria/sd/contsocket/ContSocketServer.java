package es.deusto.ingenieria.sd.contsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContSocketServer {

	private static int numClients = 0;

	// syncrhonized list protects the internal state of the list from being
	// corrupted by concurrent accessories.
	private static final Map<LocalDate, Double> capacitymap = Collections.synchronizedMap(new HashMap<>());

	public static Map<LocalDate, Double> getAssignments(LocalDate date) {

		return new HashMap<>(capacitymap); // Devuelve una copia para evitar modificación externa

	}

	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println(" # Usage: ContSocketServer [PORT]");
			System.exit(1);
		}

		// args[1] = Server socket port
		int serverPort = Integer.parseInt(args[0]);

		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - ContSocketServer: Waiting for connections '"
					+ tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort() + "' ...");

			while (true) {
				ContSocketService service = new ContSocketService(tcpServerSocket.accept(), capacitymap);
				service.start();
				System.out.println(" - ContSocketServer: New client connection accepted. Client number: " + ++numClients);
			}
		} catch (IOException e) {
			System.err.println("# ContSocketServer: IO error:" + e.getMessage());
		}
	}
}