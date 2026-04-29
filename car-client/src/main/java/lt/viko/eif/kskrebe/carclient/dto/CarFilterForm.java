package lt.viko.eif.kskrebe.carclient.dto;

/**
 * Paieškos ir filtrų forma automobilių sąrašui.
 */
public class CarFilterForm {

    private String q;
    private Boolean available;
    private Float minPrice;
    private Float maxPrice;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }
}

