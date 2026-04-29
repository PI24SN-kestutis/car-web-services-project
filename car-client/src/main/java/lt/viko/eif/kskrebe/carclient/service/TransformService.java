package lt.viko.eif.kskrebe.carclient.service;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Servisas XML transformacijoms:
 * <ul>
 *     <li>XML -&gt; HTML su XSL</li>
 *     <li>XML -&gt; PDF su XSL-FO</li>
 * </ul>
 */
@Service
public class TransformService {

    private final Path outputDirectory;
    private final Resource htmlXsl;
    private final Resource pdfXsl;

    /**
     * @param outputDirectory išvesties katalogas
     * @param htmlXsl XSL šablonas HTML transformacijai
     * @param pdfXsl XSL-FO šablonas PDF transformacijai
     */
    public TransformService(
            @Value("${carclient.output-dir}") String outputDirectory,
            @Value("classpath:xsl/cars-to-html.xsl") Resource htmlXsl,
            @Value("classpath:xsl/cars-to-pdf.fo.xsl") Resource pdfXsl
    ) {
        this.outputDirectory = Path.of(outputDirectory);
        this.htmlXsl = htmlXsl;
        this.pdfXsl = pdfXsl;
    }

    /**
     * Paverčia XML failą į HTML pagal XSL šabloną.
     *
     * @param xmlPath XML failo kelias
     * @return sugeneruoto HTML failo kelias
     */
    public Path transformXmlToHtml(Path xmlPath) {
        try {
            Files.createDirectories(outputDirectory);
            Path output = outputDirectory.resolve("cars.html");

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(htmlXsl.getInputStream()));
            transformer.transform(new StreamSource(xmlPath.toFile()), new StreamResult(output.toFile()));
            return output;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create HTML file using XSL.", exception);
        }
    }

    /**
     * Paverčia XML failą į PDF pagal XSL-FO šabloną.
     *
     * @param xmlPath XML failo kelias
     * @return sugeneruoto PDF failo kelias
     */
    public Path transformXmlToPdf(Path xmlPath) {
        try {
            Files.createDirectories(outputDirectory);
            Path output = outputDirectory.resolve("cars.pdf");

            FopFactory fopFactory = FopFactory.newInstance(Path.of(".").toUri());
            FOUserAgent userAgent = fopFactory.newFOUserAgent();

            try (OutputStream outputStream = Files.newOutputStream(output)) {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outputStream);
                Transformer transformer = TransformerFactory.newInstance()
                        .newTransformer(new StreamSource(pdfXsl.getInputStream()));
                Source source = new StreamSource(xmlPath.toFile());
                Result result = new SAXResult(fop.getDefaultHandler());
                transformer.transform(source, result);
            }
            return output;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to create PDF file using XSL-FO.", exception);
        }
    }
}
