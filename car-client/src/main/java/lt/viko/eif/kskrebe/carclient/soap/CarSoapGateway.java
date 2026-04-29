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

    /**
     * Grąžina vieną automobilį pagal identifikatorių.
     *
     * @param id automobilio identifikatorius
     * @return automobilis, jei rastas
     */
    Car getCarById(Long id);

    /**
     * Sukuria arba atnaujina automobilį (upsert).
     *
     * @param car SOAP automobilio objektas
     * @return iš serverio grąžintas automobilis po išsaugojimo
     */
    Car upsertCar(Car car);

    /**
     * Pašalina automobilį pagal identifikatorių.
     *
     * @param id automobilio identifikatorius
     */
    void deleteCar(Long id);
}
