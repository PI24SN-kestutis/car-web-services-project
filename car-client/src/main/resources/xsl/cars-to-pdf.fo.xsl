<?xml version="1.0" encoding="UTF-8"?>
<!-- XSL-FO šablonas, kuris paverčia cars.xml duomenis į PDF dokumentą. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm" margin="1.5cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="16pt" font-weight="bold" space-after="10pt">Cars Export (XSL-FO)</fo:block>

                    <fo:table table-layout="fixed" width="100%" border="0.5pt solid black">
                        <fo:table-column column-width="2cm"/>
                        <fo:table-column column-width="3cm"/>
                        <fo:table-column column-width="3cm"/>
                        <fo:table-column column-width="2cm"/>
                        <fo:table-column column-width="3cm"/>
                        <fo:table-column column-width="3cm"/>

                        <fo:table-header>
                            <fo:table-row background-color="#eeeeee">
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>ID</fo:block></fo:table-cell>
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>Make</fo:block></fo:table-cell>
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>Model</fo:block></fo:table-cell>
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>Year</fo:block></fo:table-cell>
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>Price</fo:block></fo:table-cell>
                                <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block>Available</fo:block></fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>

                        <fo:table-body>
                            <xsl:for-each select="cars/car">
                                <fo:table-row>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="id"/></fo:block></fo:table-cell>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="make"/></fo:block></fo:table-cell>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="model"/></fo:block></fo:table-cell>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="year"/></fo:block></fo:table-cell>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="price"/></fo:block></fo:table-cell>
                                    <fo:table-cell padding="4pt" border="0.5pt solid black"><fo:block><xsl:value-of select="available"/></fo:block></fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>

