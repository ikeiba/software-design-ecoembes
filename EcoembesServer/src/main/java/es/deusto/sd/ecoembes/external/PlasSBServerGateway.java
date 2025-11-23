package es.deusto.sd.ecoembes.external;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.AssignmentExternalNotificationDTO;

@Component("PlasSB Ltd.")
@Lazy
public class PlasSBServerGateway implements IServiceGateway {

  // API Server Host and Port NOT hard-coded: Defined in application.properties
  @Value("${gateway.PlasSB.ip}")
  private String serverIP;

  @Value("${gateway.PlasSB.port}")
  private int serverPort;

  private final ObjectMapper mapper = new ObjectMapper();

  public PlasSBServerGateway() {
  }

  @Override
  public double getCapacity(LocalDate date) {
    String datestr = date.toString();
    double capacity = 0.0;
    try {
      String urlStr = String.format("http://%s:%d/capacity?date=%s", serverIP, serverPort, datestr);
      URL url = new URL(urlStr);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("Accept", "application/json");
      con.setConnectTimeout(3000);
      con.setReadTimeout(3000);

      int status = con.getResponseCode();
      if (status == 200) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
          String line = br.readLine();
          if (line != null && !line.isBlank()) {
            capacity = mapper.readValue(line, Double.class);
          }
        }
      } else {
        System.err.println("PlasSB HTTP getCapacity returned status " + status);
      }
      con.disconnect();
    } catch (UnknownHostException e) {
      System.err.println("# PlasSB HTTP: Host error: " + e.getMessage());
    } catch (EOFException e) {
      System.err.println("# PlasSB HTTP: EOF error: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("# PlasSB HTTP: IO error: " + e.getMessage());
    }
    return capacity;
  }

  @Override
  public AssignmentExternalNotificationDTO assignDumpsterToPlant(
      AssignmentExternalNotificationDTO assignmentExternalNotificationDTO) {

    AssignmentExternalNotificationDTO assignmentExternalDTO_ret = null;
    try {
      String urlStr = String.format("http://%s:%d/assign", serverIP, serverPort);
      URL url = new URL(urlStr);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json; utf-8");
      con.setRequestProperty("Accept", "application/json");
      con.setDoOutput(true);
      con.setConnectTimeout(3000);
      con.setReadTimeout(5000);

      try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
        String json = mapper.writeValueAsString(assignmentExternalNotificationDTO);
        bw.write(json);
        bw.flush();
      }

      int status = con.getResponseCode();
      if (status == 200 || status == 201) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
          String responseJson = sb.toString();
          assignmentExternalDTO_ret = mapper.readValue(responseJson, AssignmentExternalNotificationDTO.class);
        }
      } else {
        System.err.println("PlasSB HTTP assign returned status " + status);
      }
      con.disconnect();

    } catch (UnknownHostException e) {
      System.err.println("# PlasSB HTTP: Host error: " + e.getMessage());
    } catch (EOFException e) {
      System.err.println("# PlasSB HTTP: EOF error: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("# PlasSB HTTP: IO error: " + e.getMessage());
    }

    return assignmentExternalDTO_ret;

  }
}
