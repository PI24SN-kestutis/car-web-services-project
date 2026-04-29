package lt.viko.eif.kskrebe.carserver.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Automobilio domeno modelis.
 *
 * <p>Ši klasė demonstruoja skirtingus Java primityvių duomenų tipus:</p>
 * <ul>
 *   <li>{@code String} – tekstiniai laukai (gamintojas, modelis)</li>
 *   <li>{@code int} – sveikasis skaičius (metai)</li>
 *   <li>{@code float} – slankiojo kablelio skaičius (kaina)</li>
 *   <li>{@code boolean} – loginė reikšmė (prieinamumas)</li>
 *   <li>{@code char} – vienas simbolis (spalvos kodas)</li>
 * </ul>
 *
 * <p>JPA anotacijos tvarko duomenų bazės lentelę {@code cars}.</p>
 * <p>JAXB anotacijos leidžia serializuoti objektą į XML SOAP atsakymuose.</p>
 *
 * <p><b>Pastaba dėl {@code char} tipo:</b> JAXB serializuoja {@code char}
 * kaip {@code xs:unsignedShort} (skaičių). Pvz., 'R' = 82. Tai normalu –
 * klientas konvertuoja atgal su {@code (char) value}.</p>
 *
 * @author kskrebe
 * @version 1.0
 * @see CarPart
 */
@Entity
@Table(name = "cars")
@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

    /** Unikalus identifikatorius, automatiškai generuojamas duomenų bazėje. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Gamintojo pavadinimas, pvz., "Toyota", "BMW". */
    @Column(nullable = false)
    private String make;

    /** Modelio pavadinimas, pvz., "Corolla", "X5". */
    @Column(nullable = false)
    private String model;

    /** Pagaminimo metai, pvz., 2022. */
    @Column(name = "manufacture_year", nullable = false)
    private int year;

    /** Kaina eurais. Naudoja {@code float} tipą primityvių tipų demonstracijai. */
    @Column(nullable = false)
    private float price;

    /** Ar automobilis šiuo metu yra galimas pirkti. */
    @Column(nullable = false)
    private boolean available;

    /**
     * Spalvos kodas – vieno simbolio {@code char} tipas.
     * Galimos reikšmės:
     * <ul>
     *   <li>'R' – raudona (Red)</li>
     *   <li>'B' – balta (Black)</li>
     *   <li>'G' – pilka (Grey)</li>
     *   <li>'S' – sidabrinė (Silver)</li>
     * </ul>
     */
    @Column(name = "color_code", nullable = false)
    private char colorCode;

    /**
     * Automobilio dalių sąrašas.
     * Vienas automobilis gali turėti daug dalių (One-to-Many ryšys).
     * {@code CascadeType.ALL} – kai automobilis ištrinamas, ištrinamos ir jo dalys.
     * {@code FetchType.EAGER} – dalys įkraunamos kartu su automobiliu.
     */
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @XmlElementWrapper(name = "parts")
    @XmlElement(name = "part")
    private List<CarPart> parts = new ArrayList<>();

    /**
     * Tuščias konstruktorius – būtinas JPA ir JAXB veikimui.
     */
    public Car() {}

    /**
     * Pilnas konstruktorius be ID (ID generuojamas automatiškai duomenų bazės).
     *
     * @param make      gamintojo pavadinimas
     * @param model     modelio pavadinimas
     * @param year      pagaminimo metai
     * @param price     kaina eurais
     * @param available ar automobilis galimas
     * @param colorCode spalvos kodas (vienas simbolis)
     */
    public Car(String make, String model, int year, float price, boolean available, char colorCode) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.available = available;
        this.colorCode = colorCode;
    }

    // ===== Getters & Setters =====

    /**
     * Grąžina unikalų identifikatorių.
     *
     * @return automobilio ID
     */
    public Long getId() { return id; }

    /**
     * Nustato unikalų identifikatorių.
     *
     * @param id automobilio ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Grąžina gamintojo pavadinimą.
     *
     * @return gamintojas
     */
    public String getMake() { return make; }

    /**
     * Nustato gamintojo pavadinimą.
     *
     * @param make gamintojas
     */
    public void setMake(String make) { this.make = make; }

    /**
     * Grąžina modelio pavadinimą.
     *
     * @return modelis
     */
    public String getModel() { return model; }

    /**
     * Nustato modelio pavadinimą.
     *
     * @param model modelis
     */
    public void setModel(String model) { this.model = model; }

    /**
     * Grąžina pagaminimo metus.
     *
     * @return metai
     */
    public int getYear() { return year; }

    /**
     * Nustato pagaminimo metus.
     *
     * @param year metai
     */
    public void setYear(int year) { this.year = year; }

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
     * Grąžina, ar automobilis yra galimas.
     *
     * @return {@code true} – galimas, {@code false} – negali
     */
    public boolean isAvailable() { return available; }

    /**
     * Nustato automobilio prieinamumą.
     *
     * @param available prieinamumas
     */
    public void setAvailable(boolean available) { this.available = available; }

    /**
     * Grąžina spalvos kodą.
     *
     * @return spalvos kodas (vienas simbolis)
     */
    public char getColorCode() { return colorCode; }

    /**
     * Nustato spalvos kodą.
     *
     * @param colorCode spalvos kodas (vienas simbolis)
     */
    public void setColorCode(char colorCode) { this.colorCode = colorCode; }

    /**
     * Grąžina automobilio dalių sąrašą.
     *
     * @return dalių sąrašas
     */
    public List<CarPart> getParts() { return parts; }

    /**
     * Nustato automobilio dalių sąrašą.
     *
     * @param parts dalių sąrašas
     */
    public void setParts(List<CarPart> parts) { this.parts = parts; }

    /**
     * Grąžina objekto tekstinį atvaizdavimą (naudinga derinimui).
     *
     * @return objekto aprašas tekstu
     */
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", available=" + available +
                ", colorCode=" + colorCode +
                '}';
    }
}

