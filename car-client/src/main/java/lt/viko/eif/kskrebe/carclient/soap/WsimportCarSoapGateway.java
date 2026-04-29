package lt.viko.eif.kskrebe.carclient.soap;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceException;
import lt.viko.eif.kskrebe.carclient.config.SoapProperties;
import lt.viko.eif.kskrebe.carclient.ws.Car;
import lt.viko.eif.kskrebe.carclient.ws.CarService;
import lt.viko.eif.kskrebe.carclient.ws.CarWebService;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * SOAP užklausų įgyvendinimas naudojant {@code wsimport} sugeneruotas klases.
 */
@Component
public class WsimportCarSoapGateway implements CarSoapGateway {

    private final SoapProperties soapProperties;

    /**
     * @param soapProperties SOAP konfigūracijos reikšmės
     */
    public WsimportCarSoapGateway(SoapProperties soapProperties) {
        this.soapProperties = soapProperties;
    }

    /**
     * Užmezga ryšį su SOAP serveriu ir grąžina automobilių sąrašą.
     *
     * @return automobilių sąrašas iš serverio
     */
    @Override
    public List<Car> getAllCars() {
        try {
            return createPort().getAllCars();
        } catch (WebServiceException exception) {
            throw new IllegalStateException("Cannot connect to SOAP server. Is carserver running?", exception);
        }
    }

    @Override
    public Car getCarById(Long id) {
        try {
            return createPort().getCarById(id);
        } catch (WebServiceException exception) {
            throw new IllegalStateException("Cannot connect to SOAP server. Is carserver running?", exception);
        }
    }

    @Override
    public Car upsertCar(Car car) {
        try {
            return createPort().addCar(car);
        } catch (WebServiceException exception) {
            throw new IllegalStateException("Cannot connect to SOAP server. Is carserver running?", exception);
        }
    }

    @Override
    public void deleteCar(Long id) {
        try {
            createPort().deleteCar(id);
        } catch (WebServiceException exception) {
            throw new IllegalStateException("Cannot connect to SOAP server. Is carserver running?", exception);
        }
    }

    private CarWebService createPort() {
        try {
            URL wsdl = URI.create(soapProperties.wsdlUrl()).toURL();
            QName serviceQName = new QName(soapProperties.namespace(), soapProperties.serviceName());
            CarService service = new CarService(wsdl, serviceQName);
            CarWebService port = service.getCarPort();
            ((BindingProvider) port).getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    soapProperties.wsdlUrl().replace("?wsdl", "")
            );
            return port;
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Invalid WSDL URL in application properties.", exception);
        }
    }
}
