package es.deusto.sd.ecoembes.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import  es.deusto.sd.ecoembes.external.IServiceGateway;


@Component
public class ServiceGatewayFactory {

    private final Map<String, IServiceGateway> gatewayMap;

    // Spring inyecta AUTOMÁTICAMENTE todos los beans que implementan IServiceGateway
    public ServiceGatewayFactory(Map<String, IServiceGateway> gatewayMap) {
        this.gatewayMap = gatewayMap;
    }

    public IServiceGateway getGateway(String plant) {
        IServiceGateway gateway = gatewayMap.get(plant);
        if (gateway == null) {
            throw new IllegalArgumentException("Unknown gateway type: " + plant);
        }
        return gateway;
    }
}
