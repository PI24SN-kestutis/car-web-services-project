package lt.viko.eif.kskrebe.carclient.web;

import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.service.CarService;
import lt.viko.eif.kskrebe.carclient.service.ExportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * MVC testai, tikrinantys valdiklio maršrutus ir atsakus.
 */
@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private ExportService exportService;

    /**
     * Patikrina, kad pagrindinis puslapis grąžina {@code cars} šabloną su modelio duomenimis.
     */
    @Test
    void showsCarsPage() throws Exception {
        Mockito.when(carService.getAllCars())
                .thenReturn(List.of(new CarView(1L, "Audi", "A4", 2020, 1000f, true, 'A', List.of())));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attributeExists("cars"));
    }

    /**
     * Patikrina, kad XML eksporto veiksmas atlieka peradresavimą su sėkmės žinute.
     */
    @Test
    void exportsXmlAndRedirectsToIndex() throws Exception {
        Mockito.when(carService.getAllCars()).thenReturn(List.of());
        Mockito.when(exportService.exportXml(List.of())).thenReturn(Path.of("output/cars.xml"));

        mockMvc.perform(post("/export/xml"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/?msg=*"));
    }

    /**
     * Patikrina, kad HTML eksporto veiksmas atlieka peradresavimą su sėkmės žinute.
     */
    @Test
    void exportsHtmlAndRedirectsToIndex() throws Exception {
        Mockito.when(carService.getAllCars()).thenReturn(List.of());
        Mockito.when(exportService.exportHtml(List.of()))
                .thenReturn(new ExportService.ExportResult(Path.of("output/cars.xml"), Path.of("output/cars.html")));

        mockMvc.perform(post("/export/html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/?msg=*"));
    }
}
