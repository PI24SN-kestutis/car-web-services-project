package lt.viko.eif.kskrebe.carserver.repository;

import lt.viko.eif.kskrebe.carserver.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Duomenų prieigos sluoksnis automobiliams.
 *
 * <p>Spring Data JPA automatiškai sugeneruoja CRUD operacijas pagal
 * sąsajos {@link JpaRepository} metodus – nereikia rašyti SQL.</p>
 *
 * <p>Paveldimi metodai (pavyzdžiai):</p>
 * <ul>
 *   <li>{@code findAll()} – visi automobiliai</li>
 *   <li>{@code findById(id)} – pagal ID</li>
 *   <li>{@code save(car)} – išsaugoti arba atnaujinti</li>
 *   <li>{@code deleteById(id)} – ištrinti</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Randa automobilius pagal gamintojo pavadinimą.
     * Spring Data JPA sugeneruoja SQL automatiškai iš metodo pavadinimo.
     *
     * @param make gamintojo pavadinimas (pvz., "Toyota")
     * @return atitinkančių automobilių sąrašas
     */
    List<Car> findByMake(String make);

    /**
     * Randa automobilius pagal prieinamumo statusą.
     *
     * @param available {@code true} – tik galimi, {@code false} – tik negali
     * @return atitinkančių automobilių sąrašas
     */
    List<Car> findByAvailable(boolean available);
}

