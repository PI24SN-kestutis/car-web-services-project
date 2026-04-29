package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarPartView;
import lt.viko.eif.kskrebe.carclient.dto.CarView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Servisas, kuris automobilių sąrašą eksportuoja į XML failą.
 */
@Service
public class XmlExportService {

    private final Path outputDirectory;

    /**
     * @param outputDirectory katalogas, kuriame bus saugomi sugeneruoti failai
     */
    public XmlExportService(@Value("${carclient.output-dir}") String outputDirectory) {
        this.outputDirectory = Path.of(outputDirectory);
    }

    /**
     * Išsaugo automobilių sąrašą į {@code cars.xml} failą.
     *
     * @param cars automobilių sąrašas
     * @return sugeneruoto XML failo kelias
     */
    public Path exportCars(List<CarView> cars) {
        try {
            Files.createDirectories(outputDirectory);
            Path output = outputDirectory.resolve("cars.xml");

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("cars");
            document.appendChild(root);

            for (CarView car : cars) {
                Element carElement = document.createElement("car");
                append(document, carElement, "id", String.valueOf(car.id()));
                append(document, carElement, "make", safe(car.make()));
                append(document, carElement, "model", safe(car.model()));
                append(document, carElement, "year", String.valueOf(car.year()));
                append(document, carElement, "price", String.valueOf(car.price()));
                append(document, carElement, "available", String.valueOf(car.available()));
                append(document, carElement, "colorCode", String.valueOf(car.colorCode()));

                Element partsElement = document.createElement("parts");
                for (CarPartView part : car.parts()) {
                    Element partElement = document.createElement("part");
                    append(document, partElement, "id", String.valueOf(part.id()));
                    append(document, partElement, "name", safe(part.name()));
                    append(document, partElement, "partNumber", safe(part.partNumber()));
                    append(document, partElement, "price", String.valueOf(part.price()));
                    append(document, partElement, "quantity", String.valueOf(part.quantity()));
                    append(document, partElement, "available", String.valueOf(part.available()));
                    partsElement.appendChild(partElement);
                }
                carElement.appendChild(partsElement);
                root.appendChild(carElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(output.toFile()));
            return output;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to export XML file.", exception);
        }
    }

    private void append(Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
