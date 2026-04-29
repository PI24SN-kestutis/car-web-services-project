<?xml version="1.0" encoding="UTF-8"?>
<!--
    cars.xsl – XML į HTML transformacija
    =====================================
    Šis failas naudojamas kliento pusėje:
    1. Klientas gauna automobilių XML iš SOAP paslaugos
    2. Parsiunčia šį XSL failą iš serverio (http://localhost:8080/xsl/cars.xsl)
    3. Transformuoja XML į HTML naudojant javax.xml.transform (JAXP)
    4. Rodo rezultatą naršyklėje arba savo GUI

    Standartai: XSL Version 1.1
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <!-- ====================================================
         Pagrindinis šablonas – HTML puslapio struktūra
         ==================================================== -->
    <xsl:template match="/">
        <html lang="lt">
            <head>
                <meta charset="UTF-8"/>
                <title>Automobilių katalogas</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 30px;
                        background-color: #f5f5f5;
                        color: #333;
                    }
                    h1 { color: #1a1a2e; border-bottom: 3px solid #4CAF50; padding-bottom: 10px; }
                    h2 { color: #4CAF50; margin-top: 30px; }
                    h3 { color: #2196F3; }
                    .car-card {
                        background: white;
                        border-radius: 8px;
                        padding: 20px;
                        margin-bottom: 25px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    table { border-collapse: collapse; width: 100%; margin-bottom: 15px; }
                    th { background-color: #4CAF50; color: white; padding: 10px; text-align: left; }
                    td { border: 1px solid #ddd; padding: 8px; }
                    tr:nth-child(even) { background-color: #f9f9f9; }
                    .parts-table th { background-color: #2196F3; }
                    .badge-true  { color: green; font-weight: bold; }
                    .badge-false { color: red;   font-weight: bold; }
                </style>
            </head>
            <body>
                <h1>🚗 Automobilių katalogas</h1>
                <!-- Kiekvienam automobiliui taikomas car šablonas -->
                <xsl:apply-templates select="//car"/>
            </body>
        </html>
    </xsl:template>

    <!-- ====================================================
         Vieno automobilio kortelės šablonas
         ==================================================== -->
    <xsl:template match="car">
        <div class="car-card">
            <h2>
                <xsl:value-of select="make"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="model"/>
                <xsl:text> (</xsl:text>
                <xsl:value-of select="year"/>
                <xsl:text>)</xsl:text>
            </h2>

            <!-- Automobilio pagrindinė informacija -->
            <table>
                <tr><th colspan="2">Pagrindinė informacija</th></tr>
                <tr><td><b>ID</b></td><td><xsl:value-of select="id"/></td></tr>
                <tr><td><b>Gamintojas</b></td><td><xsl:value-of select="make"/></td></tr>
                <tr><td><b>Modelis</b></td><td><xsl:value-of select="model"/></td></tr>
                <tr><td><b>Metai</b></td><td><xsl:value-of select="year"/></td></tr>
                <tr><td><b>Kaina (€)</b></td><td><xsl:value-of select="price"/></td></tr>
                <tr>
                    <td><b>Galimas</b></td>
                    <td>
                        <!-- Spalvoti ženkliukai pagal reikšmę -->
                        <xsl:choose>
                            <xsl:when test="available = 'true'">
                                <span class="badge-true">✔ Taip</span>
                            </xsl:when>
                            <xsl:otherwise>
                                <span class="badge-false">✘ Ne</span>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
                <tr>
                    <td><b>Spalvos kodas</b></td>
                    <!-- colorCode JAXB serializuoja kaip skaitmenį (char → unsignedShort) -->
                    <td><xsl:value-of select="colorCode"/></td>
                </tr>
            </table>

            <!-- Automobilio dalių lentelė (rodoma tik jei yra dalių) -->
            <xsl:if test="parts/part">
                <h3>Automobilio dalys</h3>
                <table class="parts-table">
                    <tr>
                        <th>Pavadinimas</th>
                        <th>Katalogo Nr.</th>
                        <th>Kaina (€)</th>
                        <th>Kiekis</th>
                        <th>Galima</th>
                    </tr>
                    <!-- Kiekvienai daliai taikomas part šablonas -->
                    <xsl:apply-templates select="parts/part"/>
                </table>
            </xsl:if>
        </div>
    </xsl:template>

    <!-- ====================================================
         Vienos dalies eilutės šablonas
         ==================================================== -->
    <xsl:template match="part">
        <tr>
            <td><xsl:value-of select="name"/></td>
            <td><xsl:value-of select="partNumber"/></td>
            <td><xsl:value-of select="price"/></td>
            <td><xsl:value-of select="quantity"/></td>
            <td>
                <xsl:choose>
                    <xsl:when test="available = 'true'">
                        <span class="badge-true">✔ Taip</span>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="badge-false">✘ Ne</span>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>

