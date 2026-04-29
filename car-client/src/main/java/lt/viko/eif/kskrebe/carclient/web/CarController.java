package lt.viko.eif.kskrebe.carclient.web;

import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.service.CarService;
import lt.viko.eif.kskrebe.carclient.service.ExportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**
 * Valdiklis, atsakingas už pagrindinį automobilių sąrašo puslapį ir eksporto veiksmus.
 */
@Controller
public class CarController {

    private final CarService carService;
    private final ExportService exportService;

    /**
     * @param carService servisas automobilių gavimui
     * @param exportService servisas eksportui į HTML/PDF
     */
    public CarController(CarService carService, ExportService exportService) {
        this.carService = carService;
        this.exportService = exportService;
    }

    /**
     * Atvaizduoja pagrindinį puslapį su automobilių lentele.
     *
     * @param model modelis duomenų perdavimui į Thymeleaf šabloną
     * @param message vienkartinė sėkmės žinutė iš query parametro
     * @param requestError vienkartinė klaidos žinutė iš query parametro
     * @return šablono pavadinimas
     */
    @GetMapping("/")
    public String cars(
            Model model,
            @RequestParam(value = "msg", required = false) String message,
            @RequestParam(value = "err", required = false) String requestError
    ) {
        String pageError = requestError;
        try {
            List<CarView> cars = carService.getAllCars();
            model.addAttribute("cars", cars);
        } catch (Exception exception) {
            model.addAttribute("cars", List.of());
            if (pageError == null || pageError.isBlank()) {
                pageError = exception.getMessage();
            }
        }
        model.addAttribute("message", message);
        model.addAttribute("error", pageError);
        return "cars";
    }

    /**
     * Sukuria XML failą ir grąžina vartotoją į pagrindinį puslapį.
     *
     * @return peradresavimo adresas
     */
    @PostMapping("/export/xml")
    public String exportXml() {
        try {
            List<CarView> cars = carService.getAllCars();
            Path xmlPath = exportService.exportXml(cars);
            return "redirect:/?msg=" + encode("XML failas sukurtas: " + toDisplayPath(xmlPath));
        } catch (Exception exception) {
            return "redirect:/?err=" + encode(exception.getMessage());
        }
    }

    /**
     * Sukuria HTML eksportą ir grąžina vartotoją į pagrindinį puslapį.
     *
     * @return peradresavimo adresas
     */
    @PostMapping("/export/html")
    public String exportHtml() {
        return export(ExportType.HTML);
    }

    /**
     * Sukuria PDF eksportą ir grąžina vartotoją į pagrindinį puslapį.
     *
     * @return peradresavimo adresas
     */
    @PostMapping("/export/pdf")
    public String exportPdf() {
        return export(ExportType.PDF);
    }

    private String export(ExportType exportType) {
        try {
            List<CarView> cars = carService.getAllCars();
            ExportService.ExportResult result = switch (exportType) {
                case HTML -> exportService.exportHtml(cars);
                case PDF -> exportService.exportPdf(cars);
            };
            return "redirect:/?msg=" + encode("Failai sukurti: "
                    + toDisplayPath(result.xmlPath()) + " ir " + toDisplayPath(result.outputPath()));
        } catch (Exception exception) {
            return "redirect:/?err=" + encode(exception.getMessage());
        }
    }

    private String toDisplayPath(Path path) {
        Path normalized = path.normalize();
        if (normalized.getParent() == null || normalized.getParent().getFileName() == null) {
            return normalized.getFileName().toString();
        }
        return normalized.getParent().getFileName() + "/" + normalized.getFileName();
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private enum ExportType {
        HTML,
        PDF
    }
}
