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
     * Sugeneruoja tik XML failą pagal automobilių sąrašą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruoto XML failo kelias
     */
    public Path exportXml(List<CarView> cars) {
        return xmlExportService.exportCars(cars);
    }

    /**
     * Sugeneruoja XML ir HTML failus pagal automobilių sąrašą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruotų failų keliai
     */
    public ExportResult exportHtml(List<CarView> cars) {
        Path xml = xmlExportService.exportCars(cars);
        Path html = transformService.transformXmlToHtml(xml);
        return new ExportResult(xml, html);
    }

    /**
     * Sugeneruoja XML ir PDF failus pagal automobilių sąrašą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruotų failų keliai
     */
    public ExportResult exportPdf(List<CarView> cars) {
        Path xml = xmlExportService.exportCars(cars);
        Path pdf = transformService.transformXmlToPdf(xml);
        return new ExportResult(xml, pdf);
    }

    /**
     * Paprastas rezultato objektas, skirtas grąžinti XML ir tikslinio failo kelius.
     *
     * @param xmlPath XML failo kelias
     * @param outputPath galutinio failo kelias (HTML arba PDF)
     */
    public record ExportResult(Path xmlPath, Path outputPath) {
    }
}
