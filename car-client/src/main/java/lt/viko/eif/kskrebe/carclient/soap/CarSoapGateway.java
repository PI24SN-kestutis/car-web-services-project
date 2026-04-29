package lt.viko.eif.kskrebe.carclient.soap;

import lt.viko.eif.kskrebe.carclient.ws.Car;

import java.util.List;

/**
 * Abstrakcija darbui su SOAP serveriu.
 * <p>
 * Ši sąsaja leidžia servisų sluoksniui nepriklausyti nuo konkretaus
 * SOAP kliento įgyvendinimo.
 */
public interface CarSoapGateway {

    /**
     * Grąžina visų automobilių sąrašą iš SOAP paslaugos.
     *
     * @return SOAP modelio automobilių sąrašas
     */
    List<Car> getAllCars();
}
