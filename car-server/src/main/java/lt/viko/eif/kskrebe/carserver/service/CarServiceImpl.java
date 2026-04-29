package lt.viko.eif.kskrebe.carserver.service;

import lt.viko.eif.kskrebe.carserver.model.Car;
import lt.viko.eif.kskrebe.carserver.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Automobilių verslo logikos implementacija.
 *
 * <p>Realizuoja {@link CarService} sąsają, naudodama {@link CarRepository}
 * duomenų prieigai iš H2 duomenų bazės.</p>
 *
 * <p>Anotacija {@code @Service} žymi šią klasę kaip Spring komponentą –
 * Spring automatiškai sukuria jos egzempliorių ir tvarko gyvavimo ciklą.</p>
 *
 * <p>Priklausomybės įterpiamos per konstruktorių (rekomenduojamas būdas),
 * o ne per laukus – taip lengviau testuoti su Mockito.</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@Service
public class CarServiceImpl implements CarService {

    /** Duomenų prieigos komponentas, įterpiamas per konstruktorių. */
    private final CarRepository carRepository;

    /**
     * Konstruktorius su priklausomybių įterpimu (Constructor Injection).
     *
     * @param carRepository automobilio repozitorija
     */
    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Deleguoja užklausą tiesiai į repozitoriją – paprastas praėjimas.</p>
     */
    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * <p>{@link Optional} naudojamas vietoj {@code null}, kad klientas
     * būtų priverstas aiškiai apdoroti „nerastas" atvejį.</p>
     */
    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prieš išsaugojant susieja automobilio dalis su automobiliu –
     * reikia nustatyti {@code car} lauką kiekvienai daliai.</p>
     */
    @Override
    public Car saveCar(Car car) {
        // Nustatome dvikryptį ryšį: kiekviena dalis žino, kuriam automobiliui priklauso
        if (car.getParts() != null) {
            car.getParts().forEach(part -> part.setCar(car));
        }
        return carRepository.save(car);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}

