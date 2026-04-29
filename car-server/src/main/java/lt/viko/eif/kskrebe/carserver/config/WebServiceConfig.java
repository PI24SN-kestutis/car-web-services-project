package lt.viko.eif.kskrebe.carserver.config;

import lt.viko.eif.kskrebe.carserver.ws.CarWebServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.xml.ws.Endpoint;

/**
 * Apache CXF JAX-WS SOAP endpoint konfigūracija.
 *
 * <p>Ši klasė registruoja SOAP paslaugą Spring kontekste per Apache CXF.
 * CXF palaiko Spring Boot integraciją ir automatiškai sukuria HTTP servletą,
 * kuris apdoroja SOAP užklausas.</p>
 *
 * <p>Konfigūracijos parametras {@code cxf.path=/services} (iš
 * {@code application.properties}) nustato bendrą prefiksą.</p>
 *
 * <p>Galimi adresai po paleidimo:</p>
 * <ul>
 *   <li>SOAP endpoint: {@code http://localhost:8080/services/cars}</li>
 *   <li>WSDL dokumentas: {@code http://localhost:8080/services/cars?wsdl}</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 */
@Configuration
public class WebServiceConfig {

    /** CXF magistralė – centrinė Apache CXF konfigūracijos saugykla. */
    private final Bus bus;

    /** SOAP paslaugos implementacija. */
    private final CarWebServiceImpl carWebServiceImpl;

    /**
     * Konstruktorius (priklausomybės įterpiamos automatiškai per Spring).
     *
     * @param bus               Apache CXF magistralė
     * @param carWebServiceImpl SOAP paslaugos implementacija
     */
    public WebServiceConfig(Bus bus, CarWebServiceImpl carWebServiceImpl) {
        this.bus = bus;
        this.carWebServiceImpl = carWebServiceImpl;
    }

    /**
     * Registruoja automobilio SOAP endpoint adresu {@code /cars}.
     *
     * <p>Pilnas adresas: {@code /services/cars} (prefiksas + šis kelias).</p>
     *
     * @return sukonfigūruotas ir paleistas SOAP endpoint
     */
    @Bean
    public Endpoint carEndpoint() {
        // EndpointImpl – CXF implementacija, kuri prijungia servisą prie BUS
        EndpointImpl endpoint = new EndpointImpl(bus, carWebServiceImpl);
        // Skelbiame endpoint adresu /cars (pilnas: /services/cars)
        endpoint.publish("/cars");
        return endpoint;
    }
}

