package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarView;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

/**
 * Koordinuoja eksporto procesą: iš pradžių sukuria XML, tada atlieka transformaciją.
 */
@Service
public class ExportService {

    private final XmlExportService xmlExportService;
    private final TransformService transformService;

    /**
     * @param xmlExportService XML generavimo servisas
     * @param transformService XSL/XSL-FO transformacijų servisas
     */
    public ExportService(XmlExportService xmlExportService, TransformService transformService) {
        this.xmlExportService = xmlExportService;
        this.transformService = transformService;
    }

    /**
     * Sugeneruoja HTML failą pagal automobilių sąrašą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruoto HTML failo kelias
     */
    public Path exportHtml(List<CarView> cars) {
        Path xml = xmlExportService.exportCars(cars);
        return transformService.transformXmlToHtml(xml);
    }

    /**
     * Sugeneruoja PDF failą pagal automobilių sąrašą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruoto PDF failo kelias
     */
    public Path exportPdf(List<CarView> cars) {
        Path xml = xmlExportService.exportCars(cars);
        return transformService.transformXmlToPdf(xml);
    }
}
