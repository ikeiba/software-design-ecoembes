package es.deusto.sd.client.proxies;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import es.deusto.sd.client.data.Assignment;
import es.deusto.sd.client.data.Dumpster;
import es.deusto.sd.client.data.DumpsterStatus;
import es.deusto.sd.client.data.Login;
import es.deusto.sd.client.data.NewDumpster;
import es.deusto.sd.client.data.PlantCapacity;

@Service
public class RestTemplateServiceProxy implements IEcoembesServiceProxy {

    private final RestTemplate restTemplate;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RestTemplateServiceProxy.class);
    @Value("${api.base.url}")
    private String apiBaseUrl;

    public RestTemplateServiceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String login(Login credentials) {
        String url = apiBaseUrl + "/auth/login";
        try {
            return restTemplate.postForObject(url, credentials, String.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Login fallido: " + e.getStatusText());
        }
    }

    @Override
    public void logout(String token) {
        String url = apiBaseUrl + "/auth/logout";
        try {
            restTemplate.postForObject(url, token, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Logout fallido.");
        }
    }

    @Override
    public Dumpster createDumpster(NewDumpster newDumpster) {
        String url = apiBaseUrl + "/ecoembes/dumpsters";
        try {
            return restTemplate.postForObject(url, newDumpster, Dumpster.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Error creando contenedor: " + e.getResponseBodyAsString());
        }
    }

    @Override
    public List<DumpsterStatus> getDumpsterStatus(String postalCode, LocalDate date) {
        // Spring convierte automáticamente LocalDate.toString() al formato ISO (yyyy-mm-dd)
        // que es lo que espera tu servidor.
        String url = apiBaseUrl + "/ecoembes/dumpsters/status?postalCode=" + postalCode + "&date=" + date;
        
        try {
            DumpsterStatus[] response = restTemplate.getForObject(url, DumpsterStatus[].class);
            if (response != null) {
                Arrays.stream(response).forEach(ds -> logger.info("DumpsterStatus recibido: {}", ds));
            } else {
                logger.info("Server Response to get Dumpster status: response is null");
            }
            return response != null ? Arrays.asList(response) : List.of(); // Evita NPE
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 204) return List.of(); // Sin contenido
            throw new RuntimeException("Error obteniendo estado: " + e.getStatusText());
        }
    }

    @Override
    public List<PlantCapacity> getPlantsCapacity(LocalDate date) {
        String url = apiBaseUrl + "/ecoembes/plants/capacity?date=" + date;
        
        try {
            PlantCapacity[] response = restTemplate.getForObject(url, PlantCapacity[].class);
            return response != null ? Arrays.asList(response) : List.of(); // Evita NPE
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 204) return List.of();
            throw new RuntimeException("Error obteniendo capacidades.");
        }
    }

    @Override
    public Assignment assignDumpster(Assignment assignment) {
        String url = apiBaseUrl + "/ecoembes/assignments";
        try {
            return restTemplate.postForObject(url, assignment, Assignment.class);
        } catch (HttpStatusCodeException e) {
            // Capturamos el error 400 o 500 para mostrar el mensaje del servidor si es posible
            throw new RuntimeException("Error en asignación: " + e.getResponseBodyAsString());
        }
    }
}