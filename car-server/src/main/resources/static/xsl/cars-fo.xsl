<?xml version="1.0" encoding="UTF-8"?>
<!--
    cars-fo.xsl – XML į XSL-FO (PDF) transformacija
    =================================================
    XSL-FO (Formatting Objects) – W3C standartas PDF generavimui.
    Apache FOP biblioteka konvertuoja šį transformacijos rezultatą į PDF.

    Naudojimas kliento pusėje:
    1. Gauti XML iš SOAP paslaugos
    2. Transformuoti su šiuo XSL-FO failu → gauname FO dokumentą
    3. Perduoti FO dokumentą Apache FOP → gauname PDF failą

    Standartai: XSL-FO (Extensible Stylesheet Language Formatting Objects)
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" indent="yes"/>

    <!-- ====================================================
         Pagrindinis šablonas – FO dokumento struktūra
         ==================================================== -->
    <xsl:template match="/">
        <fo:root>

            <!-- ===== Puslapio maketo apibrėžimas ===== -->
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-page"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin-top="1.5cm"
                                       margin-bottom="1.5cm"
                                       margin-left="2cm"
                                       margin-right="2cm">
                    <!-- Pagrindinis turinio regionas -->
                    <fo:region-body margin-top="1.5cm" margin-bottom="1cm"/>
                    <!-- Puslapio antraštė (viršus) -->
                    <fo:region-before extent="1.2cm"/>
                    <!-- Puslapio poraštė (apačia) -->
                    <fo:region-after extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <!-- ===== Puslapio sekos turinys ===== -->
            <fo:page-sequence master-reference="A4-page">

                <!-- Antraštė: rodoma kiekvieno puslapio viršuje -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="8pt"
                              text-align="center"
                              color="#888888"
                              border-bottom="0.5pt solid #cccccc"
                              padding-bottom="3pt">
                        Automobilių katalogas – Carserver sistema
                    </fo:block>
                </fo:static-content>

                <!-- Poraštė: puslapio numeris -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="8pt"
                              text-align="center"
                              color="#888888">
                        Puslapis <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <!-- ===== Pagrindinis turinys ===== -->
                <fo:flow flow-name="xsl-region-body">

                    <!-- Dokumento pavadinimas -->
                    <fo:block font-size="20pt"
                              font-weight="bold"
                              text-align="center"
                              color="#1a1a2e"
                              space-after="20pt"
                              border-bottom="2pt solid #4CAF50"
                              padding-bottom="8pt">
                        Automobilių katalogas
                    </fo:block>

                    <!-- Generuojama data -->
                    <fo:block font-size="9pt"
                              text-align="right"
                              color="#888888"
                              space-after="15pt">
                        Dokumento metodas: XSL-FO + Apache FOP
                    </fo:block>

                    <!-- Kiekvienas automobilis -->
                    <xsl:apply-templates select="//car"/>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <!-- ====================================================
         Vieno automobilio bloko šablonas
         ==================================================== -->
    <xsl:template match="car">

        <!-- Automobilio antraštė -->
        <fo:block font-size="14pt"
                  font-weight="bold"
                  color="#4CAF50"
                  space-before="15pt"
                  space-after="6pt"
                  keep-with-next="always">
            <xsl:value-of select="make"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="model"/>
            <xsl:text> (</xsl:text>
            <xsl:value-of select="year"/>
            <xsl:text>)</xsl:text>
        </fo:block>

        <!-- Automobilio pagrindinių duomenų lentelė -->
        <fo:table table-layout="fixed" width="100%"
                  border="0.5pt solid #cccccc"
                  space-after="8pt">
            <fo:table-column column-width="35%"/>
            <fo:table-column column-width="65%"/>
            <fo:table-body>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Gamintojas</xsl:with-param>
                    <xsl:with-param name="value" select="make"/>
                </xsl:call-template>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Modelis</xsl:with-param>
                    <xsl:with-param name="value" select="model"/>
                </xsl:call-template>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Metai</xsl:with-param>
                    <xsl:with-param name="value" select="year"/>
                </xsl:call-template>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Kaina (€)</xsl:with-param>
                    <xsl:with-param name="value" select="price"/>
                </xsl:call-template>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Galimas</xsl:with-param>
                    <xsl:with-param name="value" select="available"/>
                </xsl:call-template>
                <xsl:call-template name="info-row">
                    <xsl:with-param name="label">Spalvos kodas</xsl:with-param>
                    <xsl:with-param name="value" select="colorCode"/>
                </xsl:call-template>
            </fo:table-body>
        </fo:table>

        <!-- Dalių skyrius (rodomas tik jei yra dalių) -->
        <xsl:if test="parts/part">
            <fo:block font-size="11pt"
                      font-weight="bold"
                      color="#2196F3"
                      space-before="8pt"
                      space-after="4pt">
                Automobilio dalys:
            </fo:block>

            <!-- Dalių lentelė -->
            <fo:table table-layout="fixed" width="100%"
                      border="0.5pt solid #cccccc"
                      space-after="20pt">
                <fo:table-column column-width="28%"/>
                <fo:table-column column-width="22%"/>
                <fo:table-column column-width="15%"/>
                <fo:table-column column-width="15%"/>
                <fo:table-column column-width="20%"/>

                <!-- Lentelės antraštė -->
                <fo:table-header>
                    <fo:table-row background-color="#2196F3">
                        <fo:table-cell padding="5pt">
                            <fo:block color="white" font-weight="bold" font-size="9pt">Pavadinimas</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="5pt">
                            <fo:block color="white" font-weight="bold" font-size="9pt">Katalogo Nr.</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="5pt">
                            <fo:block color="white" font-weight="bold" font-size="9pt">Kaina (€)</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="5pt">
                            <fo:block color="white" font-weight="bold" font-size="9pt">Kiekis</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="5pt">
                            <fo:block color="white" font-weight="bold" font-size="9pt">Galima</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>

                <!-- Dalių eilutės -->
                <fo:table-body>
                    <xsl:apply-templates select="parts/part"/>
                </fo:table-body>
            </fo:table>
        </xsl:if>

    </xsl:template>

    <!-- ====================================================
         Pagalbinis šablonas: automobilio informacijos eilutė
         ==================================================== -->
    <xsl:template name="info-row">
        <xsl:param name="label"/>
        <xsl:param name="value"/>
        <fo:table-row>
            <fo:table-cell padding="5pt"
                           background-color="#f0f0f0"
                           border="0.5pt solid #dddddd">
                <fo:block font-weight="bold" font-size="9pt">
                    <xsl:value-of select="$label"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell padding="5pt"
                           border="0.5pt solid #dddddd">
                <fo:block font-size="9pt">
                    <xsl:value-of select="$value"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <!-- ====================================================
         Vienos dalies eilutės šablonas lentelėje
         ==================================================== -->
    <xsl:template match="part">
        <fo:table-row>
            <fo:table-cell padding="4pt" border="0.5pt solid #dddddd">
                <fo:block font-size="9pt"><xsl:value-of select="name"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="4pt" border="0.5pt solid #dddddd">
                <fo:block font-size="9pt"><xsl:value-of select="partNumber"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="4pt" border="0.5pt solid #dddddd">
                <fo:block font-size="9pt"><xsl:value-of select="price"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="4pt" border="0.5pt solid #dddddd">
                <fo:block font-size="9pt"><xsl:value-of select="quantity"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="4pt" border="0.5pt solid #dddddd">
                <fo:block font-size="9pt"><xsl:value-of select="available"/></fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

</xsl:stylesheet>

