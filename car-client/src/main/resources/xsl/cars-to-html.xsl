<?xml version="1.0" encoding="UTF-8"?>
<!-- XSL šablonas, kuris paverčia cars.xml duomenis į paprastą HTML lentelę. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Cars Export</title>
            </head>
            <body>
                <h1>Cars Export (XSL)</h1>
                <table border="1" cellpadding="6" cellspacing="0">
                    <tr>
                        <th>ID</th>
                        <th>Make</th>
                        <th>Model</th>
                        <th>Year</th>
                        <th>Price</th>
                        <th>Available</th>
                    </tr>
                    <xsl:for-each select="cars/car">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><xsl:value-of select="make"/></td>
                            <td><xsl:value-of select="model"/></td>
                            <td><xsl:value-of select="year"/></td>
                            <td><xsl:value-of select="price"/></td>
                            <td><xsl:value-of select="available"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
