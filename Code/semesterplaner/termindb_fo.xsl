<?xml version="1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/">
        <fo:root ><!-- Ab hier werden die Formatting Object-Elemente erstellt -->
            <fo:layout-master-set>
                <fo:simple-page-master
                        master-name="Erste Seite"
                        margin-top="1cm"
                        margin-bottom="0.5cm"
                        margin-left="0.5cm"
                        margin-right="0.5cm"
                        page-width="21cm"
                        page-height="29.7cm">
                        <fo:region-body margin-top="1cm"
                                        margin-bottom="1cm"
                                        margin-left="1cm"
                                        margin-right="1cm"/>
                        <fo:region-before region-name="header" extent="3cm"/>   <!-- Kopfzeile -->
                        <fo:region-after region-name="footer" extent="0.5cm"/>  <!-- Fußzeile -->
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="Erste Seite">                       <!-- ab hier Pagecontent -->
                <fo:static-content flow-name="header" text-align="center">      <!-- Kopfzeile -->
                    <fo:block font-size="12pt">Semesterplaner</fo:block>
					<fo:block font-size="6pt">--sortiert absteigend nach Startzeitpunkt--</fo:block>
					
				</fo:static-content>
                <fo:static-content flow-name="footer" text-align="center">      <!-- Kopfzeile -->
                    <fo:block font-size="10pt">Seite <fo:page-number/> von <fo:page-number-citation ref-id="LastPage"/> </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                        <fo:block font-size="8pt">
                                <xsl:apply-templates/>
                        </fo:block>
                        <fo:block id="LastPage" />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

<!-- AB HIER DYNAMISCHER CONTENT-->

    <xsl:template match="Termin">
        
        <fo:block font-size="10pt" background-color="rgb(95%,95%,95%)" text-align="center">
            <xsl:value-of select="@tid"/>
        </fo:block>
        
        <fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="stt"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="stp"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="ort"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="vbz"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="nbz"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="typ"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="prio"/>
        </fo:block>
	  
		<fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="quelle"/>
        </fo:block>
	  
        <fo:block font-size="8pt" text-align="left">
        	<xsl:value-of select="bem"/>
        </fo:block>
	  
		<fo:block font-size="4pt" text-align="left">
        	_
        </fo:block>
	  	
    </xsl:template>


</xsl:stylesheet>