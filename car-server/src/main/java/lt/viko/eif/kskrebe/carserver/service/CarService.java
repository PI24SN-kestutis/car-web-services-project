package lt.viko.eif.kskrebe.carserver.service;

import lt.viko.eif.kskrebe.carserver.model.Car;

import java.util.List;
import java.util.Optional;

/**
 * Automobilių verslo logikos sąsaja.
 *
 * <p>Apibrėžia operacijas su automobiliais pagal SOLID principus:</p>
 * <ul>
 *   <li><b>S</b> (Single Responsibility) – sąsaja atsakinga tik už automobilio logiką</li>
 *   <li><b>O</b> (Open/Closed) – nauja implementacija gali išplėsti elgseną</li>
 *   <li><b>D</b> (Dependency Inversion) – aukštesni sluoksniai priklauso nuo sąsajos, ne implementacijos</li>
 * </ul>
 *
 * <p>Ši sąsaja yra tiltas tarp SOAP paslaugos ({@code ws} paketas)
 * ir duomenų bazės ({@code repository} paketas).</p>
 *
 * @author kskrebe
 * @version 1.0
 */
public interface CarService {

    /**
     * Grąžina visus automobilius iš duomenų bazės.
     *
     * @return automobilių sąrašas (gali būti tuščias, bet ne {@code null})
     */
    List<Car> getAllCars();

    /**
     * Randa automobilį pagal unikalų identifikatorių.
     *
     * @param id automobilio ID
     * @return {@link Optional} su automobiliu arba tuščias, jei nerastas
     */
    Optional<Car> getCarById(Long id);

    /**
     * Išsaugo naują arba atnaujina esamą automobilį.
     * Jei objektas turi ID – atnaujinamas, jei ne – sukuriamas naujas.
     *
     * @param car automobilio objektas
     * @return išsaugotas automobilis su sugeneruotu ID
     */
    Car saveCar(Car car);

    /**
     * Ištrina automobilį pagal unikalų identifikatorių.
     * Kartu ištrinamos ir visos šio automobilio dalys ({@code CascadeType.ALL}).
     *
     * @param id automobilio ID
     */
    void deleteCar(Long id);
}

