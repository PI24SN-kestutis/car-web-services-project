package lt.viko.eif.kskrebe.carclient.web;

import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.service.CarService;
import lt.viko.eif.kskrebe.carclient.service.ExportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * @return šablono pavadinimas
     */
    @GetMapping("/")
    public String cars(Model model) {
        try {
            List<CarView> cars = carService.getAllCars();
            model.addAttribute("cars", cars);
            model.addAttribute("error", null);
        } catch (Exception exception) {
            model.addAttribute("cars", List.of());
            model.addAttribute("error", exception.getMessage());
        }
        return "cars";
    }

    /**
     * Sukuria HTML eksportą ir grąžina vartotoją į pagrindinį puslapį.
     *
     * @param redirectAttributes atributai vienkartinei žinutei po peradresavimo
     * @return peradresavimo adresas
     */
    @PostMapping("/export/html")
    public String exportHtml(RedirectAttributes redirectAttributes) {
        return export(redirectAttributes, ExportType.HTML);
    }

    /**
     * Sukuria PDF eksportą ir grąžina vartotoją į pagrindinį puslapį.
     *
     * @param redirectAttributes atributai vienkartinei žinutei po peradresavimo
     * @return peradresavimo adresas
     */
    @PostMapping("/export/pdf")
    public String exportPdf(RedirectAttributes redirectAttributes) {
        return export(redirectAttributes, ExportType.PDF);
    }

    private String export(RedirectAttributes redirectAttributes, ExportType exportType) {
        try {
            List<CarView> cars = carService.getAllCars();
            Path path = switch (exportType) {
                case HTML -> exportService.exportHtml(cars);
                case PDF -> exportService.exportPdf(cars);
            };
            redirectAttributes.addFlashAttribute("message", "Failas sukurtas: " + path.toAbsolutePath());
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/";
    }

    private enum ExportType {
        HTML,
        PDF
    }
}
