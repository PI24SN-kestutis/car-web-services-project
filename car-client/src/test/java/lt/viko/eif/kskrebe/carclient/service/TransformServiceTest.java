package lt.viko.eif.kskrebe.carclient.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testas, tikrinantis ar iš XML sėkmingai sugeneruojami HTML ir PDF failai.
 */
class TransformServiceTest {

    /**
     * Laikinas katalogas testavimo failams.
     */
    @TempDir
    Path tempDir;

    /**
     * Patikrina abi transformacijas ir sugeneruotų failų egzistavimą.
     */
    @Test
    void createsHtmlAndPdfFromXml() throws Exception {
        Path xml = tempDir.resolve("cars.xml");
        Files.writeString(xml, """
                <cars>
                  <car>
                    <id>1</id>
                    <make>Audi</make>
                    <model>A4</model>
                    <year>2020</year>
                    <price>25000</price>
                    <available>true</available>
                  </car>
                </cars>
                """, StandardCharsets.UTF_8);

        TransformService transformService = new TransformService(
                tempDir.toString(),
                new ClassPathResource("xsl/cars-to-html.xsl"),
                new ClassPathResource("xsl/cars-to-pdf.fo.xsl")
        );

        Path html = transformService.transformXmlToHtml(xml);
        Path pdf = transformService.transformXmlToPdf(xml);

        assertTrue(Files.exists(html));
        assertTrue(Files.size(html) > 0);
        assertTrue(Files.exists(pdf));
        assertTrue(Files.size(pdf) > 0);
    }
}
