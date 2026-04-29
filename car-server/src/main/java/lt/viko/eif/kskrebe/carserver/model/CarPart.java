package lt.viko.eif.kskrebe.carserver.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

/**
 * Automobilio dalies domeno modelis.
 *
 * <p>Kiekviena dalis priklauso vienam automobiliui (Many-to-One ryšys).
 * Ši klasė yra priklausomas objektas – negali egzistuoti be automobilio.</p>
 *
 * <p>Lauko tipai demonstracijai:</p>
 * <ul>
 *   <li>{@code String} – pavadinimas, katalogo numeris</li>
 *   <li>{@code float} – kaina</li>
 *   <li>{@code int} – kiekis sandėlyje</li>
 *   <li>{@code boolean} – prieinamumas</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 * @see Car
 */
@Entity
@Table(name = "car_parts")
@XmlRootElement(name = "part")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarPart {

    /** Unikalus identifikatorius, automatiškai generuojamas duomenų bazėje. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Dalies pavadinimas, pvz., "Variklis", "Stabdžių diskas". */
    @Column(nullable = false)
    private String name;

    /** Dalies katalogo numeris, pvz., "ENG-001". Unikaliai identifikuoja dalį. */
    @Column(name = "part_number", nullable = false)
    private String partNumber;

    /** Dalies kaina eurais. */
    @Column(nullable = false)
    private float price;

    /** Turimas kiekis sandėlyje. */
    @Column(nullable = false)
    private int quantity;

    /** Ar dalis šiuo metu yra galima užsakyti. */
    @Column(nullable = false)
    private boolean available;

    /**
     * Automobilis, kuriam priklauso ši dalis.
     * {@code @XmlTransient} neleidžia JAXB serializuoti šio lauko,
     * taip išvengiama begalinės rekursijos (Car → CarPart → Car → ...).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    @XmlTransient
    private Car car;

    /**
     * Tuščias konstruktorius – būtinas JPA ir JAXB veikimui.
     */
    public CarPart() {}

    /**
     * Pilnas konstruktorius be ID.
     *
     * @param name        dalies pavadinimas
     * @param partNumber  katalogo numeris
     * @param price       kaina eurais
     * @param quantity    kiekis sandėlyje
     * @param available   ar galima
     */
    public CarPart(String name, String partNumber, float price, int quantity, boolean available) {
        this.name = name;
        this.partNumber = partNumber;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
    }

    // ===== Getters & Setters =====

    /**
     * Grąžina unikalų identifikatorių.
     *
     * @return dalies ID
     */
    public Long getId() { return id; }

    /**
     * Nustato unikalų identifikatorių.
     *
     * @param id dalies ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Grąžina dalies pavadinimą.
     *
     * @return pavadinimas
     */
    public String getName() { return name; }

    /**
     * Nustato dalies pavadinimą.
     *
     * @param name pavadinimas
     */
    public void setName(String name) { this.name = name; }

    /**
     * Grąžina katalogo numerį.
     *
     * @return katalogo numeris
     */
    public String getPartNumber() { return partNumber; }

    /**
     * Nustato katalogo numerį.
     *
     * @param partNumber katalogo numeris
     */
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

    /**
     * Grąžina kainą eurais.
     *
     * @return kaina
     */
    public float getPrice() { return price; }

    /**
     * Nustato kainą eurais.
     *
     * @param price kaina
     */
    public void setPrice(float price) { this.price = price; }

    /**
     * Grąžina kiekį sandėlyje.
     *
     * @return kiekis
     */
    public int getQuantity() { return quantity; }

    /**
     * Nustato kiekį sandėlyje.
     *
     * @param quantity kiekis
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Grąžina, ar dalis yra galima.
     *
     * @return {@code true} – galima, {@code false} – negali
     */
    public boolean isAvailable() { return available; }

    /**
     * Nustato dalies prieinamumą.
     *
     * @param available prieinamumas
     */
    public void setAvailable(boolean available) { this.available = available; }

    /**
     * Grąžina susijusį automobilį.
     *
     * @return automobilis
     */
    public Car getCar() { return car; }

    /**
     * Nustato susijusį automobilį.
     *
     * @param car automobilis
     */
    public void setCar(Car car) { this.car = car; }

    /**
     * Grąžina objekto tekstinį atvaizdavimą (naudinga derinimui).
     *
     * @return objekto aprašas tekstu
     */
    @Override
    public String toString() {
        return "CarPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", available=" + available +
                '}';
    }
}

