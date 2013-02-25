<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Formatiert angenehm lesbare XML-Dateien(Zeilenumbrueche Tabulatoren etc)-->
<!-- Telefonbuch v0.4 -->
<!-- 09.02.2010 Marcel Kirbst IF08w1-B -->
<!-- Quelle: http://www.dpawson.co.uk/xsl/sect2/pretty.html#d8578e55 -->

   <xsl:output method="xml"/>
   <xsl:param name="indent-increment" select="'    '" />

   <xsl:template match="*">
      <xsl:param name="indent" select="'&#xA;'"/>

      <xsl:value-of select="$indent"/>
      <xsl:copy>
        <xsl:copy-of select="@*" />
        <xsl:apply-templates>
          <xsl:with-param name="indent"
               select="concat($indent, $indent-increment)"/>
        </xsl:apply-templates>
        <xsl:if test="*">
          <xsl:value-of select="$indent"/>
        </xsl:if>
      </xsl:copy>
   </xsl:template>

   <xsl:template match="comment()|processing-instruction()">
      <xsl:copy />
   </xsl:template>

   <!-- WARNING: this is dangerous. Handle with care -->
   <xsl:template match="text()[normalize-space(.)='']"/>

</xsl:stylesheet>
