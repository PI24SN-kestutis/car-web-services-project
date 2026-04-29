package lt.viko.eif.kskrebe.carclient.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Formos modelis automobiliui ir su juo susietoms detalėms redaguoti.
 */
public class CarForm {

    private Long id;
    private String make;
    private String model;
    private Integer year;
    private Float price;
    private Boolean available;
    private String colorCode;
    private List<CarPartForm> parts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<CarPartForm> getParts() {
        return parts;
    }

    public void setParts(List<CarPartForm> parts) {
        this.parts = parts;
    }
}

