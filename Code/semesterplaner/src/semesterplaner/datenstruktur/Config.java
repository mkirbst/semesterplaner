/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.datenstruktur;

import java.util.*;
import java.io.*;
import java.text.*;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

/**
 * Klasse Config beinhaltet die Attribute der Konfiguration sowie die Zugriffs-
 * Lade- und Speicherfunktionen.
 * @author l
 */
public class Config {

    /**
     * Klassenattribute
     */

    /**
     *
     */
    public static final boolean DEBUG = true;
    /**
     *
     */
    public static final String SaveFile = "TERMINE.saf";
    /**
     *
     */
    public String codierung_iCal = "UTF-8";

    //Klassenattribute

        private boolean     autoAkt;
	private Date        naechsteArch;
	private String      SemGr;
	private String      ServerUrl;
	private boolean     autoKonflLsg;
	private String      configPfad;
	private String      speicherPfad;
	private String      tempPfad;
	private boolean     dirty;
        private boolean     unsave;

        /**
         * Konstruktor zum laden der Defaultwerte
         */
        public Config()
        {
            autoAkt = false;
            naechsteArch = new Date();
            naechsteArch.setYear(2011);
            naechsteArch.setMonth(3);
            naechsteArch.setDate(1);
            SemGr = "IF08w1-B";
            ServerUrl="https://www.intranet.hs-mittweida.da";
            autoKonflLsg = false;
            configPfad = System.getProperty("user.dir") + System.getProperty("file.separator");
            speicherPfad = System.getProperty("user.dir") + System.getProperty("file.separator");
            tempPfad = System.getProperty("user.dir") + System.getProperty("file.separator");
            dirty = false;
            unsave = false;
        }

       
        /**
         * Speichern der Config
         */
        public void speichereConfig()
        {
            writeXml(configPfad + "config.xml", createDom());
	}

        /**
         * Schreiben der XML Datei
         * @param XmlFile
         * @param doc
         */
        public static void writeXml(String XmlFile, Document doc)
	{
		try
		{
			FileWriter writer = new FileWriter(new File(XmlFile));
			OutputFormat formatter = new OutputFormat();
			formatter.setEncoding("ISO-8859-1");
			formatter.setIndenting(true);
			formatter.setIndent(2);
			formatter.setLineSeparator("\r\n");
			XMLSerializer serializer = new XMLSerializer(writer, formatter);
			serializer.serialize(doc);
			System.out.println("...\nDie XML-Datei wurde erfolgreich geschrieben!");
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

        /**
         * Erstellen des Inhaltes des XML - Dokumentes
         * @return Dokumentdaten
         */
        public Document createDom()
	{
		Document doc = null;

		DOMImplementationImpl domImpl = new DOMImplementationImpl();
		// Erzeugen des DOM-Dokumentes inklusive Wurzel-Element
		doc = domImpl.createDocument(null, "config", null);
		// Element des ersten levels nach der Wurzel erzeugen
		Element elconf = doc.createElement("config");
                
                if (isAutoAkt())        elconf.setAttribute("automatischeAktualisierung", "true");
                else                    elconf.setAttribute("automatischeAktualisierung", "false");
                
                if (isAutoKonflLsg())   elconf.setAttribute("automatischeKonfliktloesung", "true");
                else                    elconf.setAttribute("automatischeKonfliktloesung", "false");
                
                if (isDirty())          elconf.setAttribute("Dirty", "true");
                else                    elconf.setAttribute("Dirty", "false");

                elconf.setAttribute("Seminargruppe", getSemGr());
                elconf.setAttribute("ServerUrl", getServerUrl());
                elconf.setAttribute("speicherPfad", getSpeicherPfad());
                elconf.setAttribute("tempPfad", getTempPfad());
		// Childs hinzufügen, in Reihenfolge
		
		// Element Buch in das Dokument einfügen
		doc.getDocumentElement().appendChild(elconf);

		// DOM-Dokument ist jetzt gefüllt
		return doc;
	}

        /**
         * Funktion zum Laden der XML - Datei
         * @return Datei gefunden?
         */
        public boolean ladeConfig(boolean isValidate)
        {
            File file = new File(configPfad + "config.xml");
            Document conxml;
            if(file.exists())
            {
                System.out.println("Datei existiert!");
                DOMParser DomParser = new DOMParser();
                try
                {
                    // Generelles Validieren einschalten
                    DomParser.setFeature("http://xml.org/sax/features/validation", isValidate);

                    //whitespace ignorieren
                    DomParser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);

                    // diese Eigenschaft wird auf die W3C-Schemasprache eingestellt
                    String propertySchemaLanguage="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
                    String w3cSchema = "http://www.w3.org/2001/XMLSchema";

                    // Speziell gegen Schema validieren
                    DomParser.setFeature("http://apache.org/xml/features/validation/schema", isValidate);

                    // Spezielle Schema-Sprache einstellen , hier W3C-Schema
                    DomParser.setProperty(propertySchemaLanguage, w3cSchema);

                    // Dokument parsen und einlesen in DOM
                    DomParser.parse(configPfad + "config.xml");
                    System.out.println("DOM-Reading and -Parsing erfolgreich");
                    String rootElement = DomParser.getDocument().getDocumentElement().getNodeName();
                    System.out.println("Das Wurzelelement heißt:" + rootElement);
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                conxml = DomParser.getDocument();
                Node startNode = conxml.getDocumentElement();
                show (startNode, 1);

                return true;
            }
            else
            {
                System.out.println("Datei existiert nicht!");
                return false;
            }
	}

        void show(Node node, int level)
	{
	  NamedNodeMap al = node.getAttributes();
	  if (al != null)
	  {
	    for (int i = 0; i<al.getLength(); i++)
	    {
	      Node attrNode = al.item(1);
	      String attribInfo = "AttributName [" + attrNode.getNodeName() + "] Wert [" + attrNode.getNodeValue() + "]";
	      for(int l = 0; l < level; l++) System.out.print("  ");
	      System.out.println("attribut info: " + attribInfo);
	    }
	  }
	  NodeList nl = node.getChildNodes();
	  for(int i = 0; i < nl.getLength(); i++)
	  {
	     Node ndChild = nl.item(i);
	     String elementName = ndChild.getNodeName();
             //lesen der Elemente
             if (level > 1);
              {
                 if (elementName.equals("automatischeAktualisierung"))
                 {
                     boolean help;
                     help = ndChild.getTextContent().equals("true");
                     setAutoAkt(help);
                  }
                 if (elementName.equals("naechsteArchiverung"))
                 {
                     Date help = new Date();
                     int year = 0;
                     String yyyy = ndChild.getTextContent().substring(0, 3);
                     try {
                     year = Integer.parseInt (yyyy);
                     } catch (Exception E){
                     
                     }
                     String mm = ndChild.getTextContent().substring(5, 6);
                     int month = 0;
                     try {
                     month = Integer.parseInt (mm);
                     } catch (Exception E){
                     
                     }
                     String dd = ndChild.getTextContent().substring(8, 9);
                     int day = 0;
                     try {
                     day = Integer.parseInt (dd);
                     } catch (Exception E){
                     }
                     help.setYear(year);
                     help.setMonth(month);
                     help.setDate(1);
                     setNaechsteArch(help);
                  }
                 if (elementName.equals("Seminargruppe"))
                 {
                     setSemGr(ndChild.getTextContent());
                 }
                 if (elementName.equals("ServerUrl"))
                 {
                     setServerUrl(ndChild.getTextContent());
                  }
                 if (elementName.equals("automatischeKonfliktloesung"))
                 {
                     boolean help;
                     help = ndChild.getTextContent().equals("true");
                     setAutoKonflLsg(help);
                  }
                 if (elementName.equals("speicherPfad"))
                 {
                     setSpeicherPfad(ndChild.getTextContent());
                 }
                 if (elementName.equals("TempPfad"))
                 {
                     setTempPfad(ndChild.getTextContent());
                 }
                 if (elementName.equals("dirty"))
                 {
                     boolean help;
                     help = ndChild.getTextContent().equals("true");
                     setDirty(help);
                 }
              }
	  }
        }

        /**
         * Laden der Standardeinstellungen
         */
        public void loadDefault()
        {
            // Standardeinstellungen laden
            setAutoAkt(false);
            naechsteArch.setYear(2011);
            naechsteArch.setMonth(3);
            naechsteArch.setDate(1);
            setSemGr("IF08w1-B");
            setServerUrl("https://www.intranet.hs-mittweida.da");
            setAutoKonflLsg(false);
            configPfad      = System.getProperty("user.dir") + System.getProperty("file.separator") + "config.xml";
            speicherPfad    = System.getProperty("user.dir") + System.getProperty("file.separator") + "termine.ics";
            tempPfad        = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp.ics";
            dirty = false;
            unsave = false;
        }

        /**
         * Konverteriung eiens Strings in eienen boolean Wert
         * @param Eingabestring
         * @return Booleanwert
         */
        public boolean convertStringToBoolean(String in)
        {
            boolean boo = Boolean.parseBoolean(in);
            return boo;
        }

        /**
         * Get und Setmethoden
         */

        public boolean isAutoAkt()
        {
		return autoAkt;
	}

        public void setAutoAkt(final boolean autoAkt)
        {
		this.autoAkt = autoAkt;
	}

        public Date getNaechsteArch()
        {
		return naechsteArch;
	}

        /**
         * Setzen der Nächsten Archivierung mit Datum
         */

        public void setNaechsteArch(final Date naechsteArch)
        {
		this.naechsteArch = naechsteArch;
	}

        /**
         * Setzen der Nächsten Archivierung mit der Berechnung es ersten Tages
         * des neuen Semesters
         */
        public void setNaechsteArch()
        {
            Date aktDate = new Date();
            int mon = aktDate.getMonth();
            int year;
            int mont =  3;
            if (mon < 9)
            {
                year = aktDate.getYear();
                if (mon < 3)
                {
                    //Wintersemester (Janauar - Februar)
                    mont = 3;
                }
                else
                {
                    //Sommersemster
                    mont = 9;
                }
            }
            else
            {
                //Wintersmester(Oktober - Dezember)
                year = aktDate.getYear() + 1;
                mont = 3;
            }
            naechsteArch.setYear(year);
            naechsteArch.setMonth(mont);
            naechsteArch.setDate(1);
        }


        public String getSemGr() {
		return SemGr;
	}

        public void setSemGr(final String SemGr) {
		this.SemGr = SemGr;
	}

        public String getServerUrl() {
		return ServerUrl;
	}

        public void setServerUrl(final String ServerUrl) {
		this.ServerUrl = ServerUrl;
	}

        public boolean isAutoKonflLsg() {
		return autoKonflLsg;
	}

        public void setAutoKonflLsg(final boolean autoKonflLsg) {
		this.autoKonflLsg = autoKonflLsg;
	}

        public String getConfigPfad() {
		return configPfad;
	}

        public void setConfigPfad(final String configPfad) {
		this.configPfad = configPfad;
	}

        public String getSpeicherPfad() {
		return speicherPfad;
	}

        public void setSpeicherPfad(final String speicherPfad) {
		this.speicherPfad = speicherPfad;
	}

        public String getTempPfad() {
		return tempPfad;
	}

        public void setTempPfad(final String tempPfad)
        {
		this.tempPfad = tempPfad;
	}

        public boolean isDirty()
        {
		return dirty;
	}

        public boolean getDirty()
        {
		return dirty;
	}


        public void setDirty(final boolean dirty)
        {
		this.dirty = dirty;
	}

        public boolean isUnsave()
        {
		return unsave;
	}

        public void setUnsave(final boolean dirty)
        {
		this.unsave = dirty;
	}

        /**
         * Debugfunktion zum Prüfen der Attribute
         */
        public void ausgabe()
        {
            //Debugfunktion
            System.out.println(autoAkt);
            System.out.println(naechsteArch.getYear());
            System.out.println(naechsteArch.getMonth());
            System.out.println(naechsteArch.getDate());
            System.out.println(SemGr);
            System.out.println(ServerUrl);
            System.out.println(autoKonflLsg);
            System.out.println(configPfad);
            System.out.println(speicherPfad);
            System.out.println(tempPfad);
            System.out.println(dirty);
            System.out.println(unsave);
        }
}