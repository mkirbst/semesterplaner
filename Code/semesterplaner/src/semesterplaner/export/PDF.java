/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import semesterplaner.datenstruktur.*;
//XML
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.*;
import org.w3c.dom.Document.*;
import org.w3c.dom.Node.*;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;
import org.apache.fop.apps.FOUserAgent;
import org.xml.sax.SAXException;
//PDF-FOP
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import semesterplaner.exceptions.ContainerException;

/**PDF.java
 * Klasse um aus einem übergebenen Terminsatz ein Pdf zu generieren
 * @author m
 */
public class PDF extends ExportClass  {

    Terminsatz pts;

    /**
     * Konstruktor
     * @param path Speicherpfad für das Pdf
     * @param ts Terminsatz aus dem Pdf generiert werden soll
     */
    public PDF(String path, Terminsatz ts)
    {
        super(path);
        pts = ts;
    }

    /**
     * {@inheritDoc}
     *
     * Ruft die Funktion exportPdf() auf.
     *
     * @param ts Bleibt ungenutzt, da Terminsatz bei Konstruktor uebergeben.
     */
    @Override
    public void proceed(TreeSet<Termin> ts) throws IOException, ContainerException {
        try {
            exportPdf(filename);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ContainerException(ex);
        }
    }

    /**
     * Exportiert das PDF.
     * @param filename
     * @throws FileNotFoundException
     * @throws FOPException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ParseException
     */
    protected void exportPdf(String filename) throws FileNotFoundException, FOPException, TransformerConfigurationException, TransformerException, IOException, ParserConfigurationException, ParseException  {
     //1.Schritt: lege temporäre XML-Datei zur PDF-Generierung an
     String tmpxml = "tmpxmlforpdfcreation.xml";
     Iterator itr = pts.tBeginntZwischen("1970.1.1-00:00:01", "2070.12.31-23:59:59").iterator();
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
     DocumentBuilder db = dbf.newDocumentBuilder();
     Document document = db.newDocument();
     document.setXmlStandalone(true);
     Element termindb = document.createElement("termindb");
     document.appendChild(termindb);
     termindb.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
     termindb.setAttribute("xsi:noNamespaceSchemaLocation","termindb.xsd");
     Termin t;
     
     while(itr.hasNext()) {          //iteriere durch HashMap-Werte
         t = (Termin)itr.next();
         Element Termin = (Element)termindb.appendChild(document.createElement("Termin"));
         Termin.setAttribute("tid",t.getBez());
         //if(!t.getBez().isEmpty())  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element bez = (Element)Termin.appendChild(document.createElement("bez"));
             bez.setTextContent("Bezeichnung: " + t.getBez());
         //}
         if(!t.getSttStr().isEmpty())  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element stt = (Element)Termin.appendChild(document.createElement("stt"));
             stt.setTextContent("Startzeitpunkt: " + t.getSttStr());
         }
         if(!t.getStpStr().isEmpty())  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element stp = (Element)Termin.appendChild(document.createElement("stp"));
             stp.setTextContent("Endzeitpunkt: " + t.getStpStr());
         }
         if(!t.getOrt().isEmpty())  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element ort = (Element)Termin.appendChild(document.createElement("ort"));
             ort.setTextContent("Ort: " + t.getOrt());
         }
         if(t.getVbz() > 0)  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element vbz = (Element)Termin.appendChild(document.createElement("vbz"));
             vbz.setTextContent("Vorbereitungszeit: " + t.getVbz().toString());
         }
         if(t.getNbz() > 0)  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element nbz = (Element)Termin.appendChild(document.createElement("nbz"));
             nbz.setTextContent("Nachbereitungszeit: " + t.getNbz().toString());
         }
         if(!t.getBem().isEmpty())  {
             Element bem = (Element)Termin.appendChild(document.createElement("bem"));
             bem.setTextContent("Bemerkungen: " + t.getBem());
         }
         if(!t.getTyp().isEmpty())  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element typ = (Element)Termin.appendChild(document.createElement("typ"));
             typ.setTextContent("Termintyp: " + t.getTyp());
         }
         if(t.getPrio() > 0)  {    //optionale Elemente nur anlegen wenn entspr. Content vorhanden
             Element prio = (Element)Termin.appendChild(document.createElement("prio"));
             prio.setTextContent("Priorität: " + t.getPrio());
         }
         if(t.getQuelle() > 0)  {
             Element quelle = (Element)Termin.appendChild(document.createElement("quelle"));
             quelle.setTextContent("Quelle: " + t.getQuelle());
         }
     }

     FileInputStream fis = new FileInputStream("xml.xsl");
     Source xsltVorlage = new StreamSource(fis);


     TransformerFactory tF = TransformerFactory.newInstance();
     Transformer tf = null;
     tf = tF.newTransformer(xsltVorlage);
     tf.setOutputProperty(OutputKeys.ENCODING,"UTF-8");

    
     DOMSource source = new DOMSource(document);
     FileOutputStream fos = new FileOutputStream(tmpxml);
     StreamResult result = new StreamResult(fos);
     tf.transform(source, result);
     fos.close();

     //2.Schritt: aus XML das PDF erstellen
     File xsltfile = new File("termindb_fo.xsl");
     //File pdffile = new File(filename);
     File pdffile = new File(filename);
     FopFactory fopFactory = FopFactory.newInstance();
     FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
     OutputStream out = new FileOutputStream(pdffile);
     out = new BufferedOutputStream(out);
     Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
     TransformerFactory factory = TransformerFactory.newInstance();
     Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
     transformer.setParameter("versionParam", "2.0");
     Source src = new StreamSource(tmpxml);
     SAXResult res = new SAXResult(fop.getDefaultHandler());
     transformer.transform(src, res);
     out.close();
     File f = new File(tmpxml); //loesche temporaeres XML wieder
     f.delete();
  }



}
