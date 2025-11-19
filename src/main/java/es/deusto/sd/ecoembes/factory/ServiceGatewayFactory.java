package es.deusto.sd.ecoembes.factory;

import org.springframework.stereotype.Component;

import  es.deusto.sd.ecoembes.external.IServiceGateway;
import  es.deusto.sd.ecoembes.external.ContSocketGateway;

@Component
public class ServiceGatewayFactory {

    public IServiceGateway createGateway(String gateway) {
        if (gateway.equals("Cont")) {
            return new ContSocketGateway();
        } /*else if (gateway.equals("PlassSB")) {
            // Implementar la creación del PlassSB gateway
            return new PlassSBGateway();} /* */
        else {
            throw new IllegalArgumentException("Unknown gateway type: " + gateway);
        }
    }
}
