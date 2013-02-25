package semesterplaner.importt;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import semesterplaner.exceptions.ContainerException;
import semesterplaner.libs.Downloader;
import semesterplaner.datenstruktur.*;
import semesterplaner.libs.FunctionCollection;

/**
 * Importiert den HTWM Stundenplan mit Hilfe einer anderen Klasse. 
 * Dieses kann durch eine Datei erfolgen, oder durch den Namen der Seminargruppe.
 * 
 * Dabei ist folgendermassen vorzugehen, wenn sie die Seminargruppe angeben wollen. Ansonsten reicht es, wenn der Pfad zur Datei angegeben wird.
 * <code>
 * HTWMStundenplanImport hi = new HTWMStundenplanImport(new Terminsatz(),HTWMStundenplanImport.IM_CAL);
 * hi.setSeminargruppe("IF08w1-B");
 * hi.downloadSeminargruppe();
 * hi.proceed();
 * hi.removeSeminargruppeVerweis();
 * </code>
 * 
 * @author Christian Vorberg
 */
public class HTWMStundenplanImport extends ImportClass 
{
	/**
	 * Die Klasse die zum Importieren genutzt werden soll.
	 */
	protected ImportClass iClass;
	/**
	 * Der Stundenplan dieser Seminargruppe soll aktualisiert werden.
	 */
	protected String seminargruppe;
		
	/**
	 * Als Importklasse soll das iCalender Format genutzt werden.
	 */
	public final static int IM_ICAL = 1;
	/**
	 * Die Importklasse ist ein binaerer Terminsatz.
	 */
	private final static int IM_BINARY = 2;
	/**
	 * Ein Statischer String, der nur kennzeichen soll, dass die Seminargruppe aus dem Internet geholt werden soll.
	 */
	private final static String USE_SEMINARGRUPPE = "<USE-SEMINARGRUPPE>";
	/**
	 * Das Postfix fuer eine Temporaere Datei
	 */
	public final static String FILE_TEMP_POSTFIX = "_temp.temp";
	
	/**
	 * Erzeugt die Aktualisierungsklasse fuer den HTWM-Stundenplan.
	 *  
	 * Es muss der Importtyp angegeben werden.
	 * 
	 * @param ts Der Terminsatz in den Importiert werden soll
	 * @param importtyp Der Importtyp. (z.B. HTWMStundenplanImport.IM_ICAL)
	 * @throws IllegalArgumentException, wenn ein falscher Importtyp angegeben wurde.
	 */
	public HTWMStundenplanImport(Terminsatz ts, int importtyp) {
		super(ts);
			
		seminargruppe = null;
		
		//Auf gueltige Klassen ueberpruefen
		if (importtyp == HTWMStundenplanImport.IM_ICAL)
			iClass = new ICalender(ts);
		else
			throw new IllegalArgumentException("Falscher Importtyp angegeben. Typ: " + importtyp);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * Wenn die Funktion setSeminargruppe() benutzt wurde, setzt diese Funktion den Pfad zur temporaeren Datei.
	 */
	public void setFilepath(String path)
	{
		this.filename = path;
	}
	
	
	/**
	 * Setzt die Seminargruppe die Importiert werden soll.
	 * 
	 * @param seminargruppe Die zu Importierende HTWM Seminargruppe.
         * @throws IllegalArgumentException Wenn der Typ nicht iCalender ist.
	 */
	public void setSeminargruppe(String seminargruppe)
	{
            if (iClass.getClass() != ICalender.class)
                throw new IllegalArgumentException("Wird nur unterstuetzt, wenn man als Importtyp iCalender angibt! Typ: " + iClass.getClass().toString());
            this.seminargruppe = seminargruppe;
            //Wir laden die Daten aus dem Inet
            this.filename = HTWMStundenplanImport.USE_SEMINARGRUPPE;
	}
	
	/**
	 * Laedt die Datei der zu Importierenden Datei des HTWM-Stundenplans.
	 * Laedt je nachdem, was beim Konstrukor angegeben wurde (.iCal Format oder xml (html?)), die entsprechende Datei. (XML ist nicht implementiert)
	 * 
	 * Der Pfad zu gedownloadeten Datei ist: seminargruppe + HTWMStundenplanImport.FILE_TEMP_POSTFIX)
	 * 
	 * @return Ob der Download Erfolgreich war.
	 * @throws IllegalArgumentException, wenn die Seminargruppe mittels setSeminargruppe() gesetzt wurde, ODER die Importierte Klasse weder XML noch das iCalender Format. (XML ist nicht implementiert)
	 */
	public boolean downloadSeminargruppe()
	{
		if (seminargruppe == null)
			throw new IllegalArgumentException("Geben sie zuerst die Seminargruppe an.");
		
		if (!(iClass.getClass() == ICalender.class))
			throw new IllegalArgumentException("Der Importtyp: " + iClass.getClass().toString() + " wird nicht unterstuetzt.");
                
		URL url = null; 
		BufferedOutputStream fw = null; //Stream, der in die Temporaere Datei schreibt
		InputStream is; //Download-Stream
		int buffer;
		
		
		if (filename.equals(HTWMStundenplanImport.USE_SEMINARGRUPPE)) //keinen Temporaeren Pfad angegeben?
			filename = seminargruppe + HTWMStundenplanImport.FILE_TEMP_POSTFIX;	
		
		if (iClass.getClass() == ICalender.class) //Nur iCalender wird unterstuezt.
		{
				try {
					//Url zusammenbasteln
					url = new URL("https://www.intranet.hs-mittweida.de/hsmw/org/tms/ics.asp?gruppe=" + seminargruppe);
					System.err.println("DEBUG: " + "https://www.intranet.hs-mittweida.de/hsmw/org/tms/ics.asp?gruppe=" + seminargruppe);
				} catch (MalformedURLException e) {
					System.err.println(e.getMessage());
					e.printStackTrace(System.err);
					return false;
				}
				
				try {	
					//OutputStream mit Datei anlegen
					fw = new BufferedOutputStream(new FileOutputStream(filename));
				} catch (FileNotFoundException e) {
					System.err.println(e.getMessage());
					e.printStackTrace(System.err);
					return false;
				}
		}
		
		//Downloaden.
		try {
			//Inputstream festlegen
			is = Downloader.download(url);
			if (is == null)
				return false;
			
			//In die Datei schreiben
			while ((buffer = is.read()) != -1)
				fw.write(buffer);
			
			is.close();
			fw.close();
		} catch (IOException e) {
			System.err.println("Message:" + e.getMessage());
			e.printStackTrace(System.err);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Importiert die Termindaten.
	 * 
	 * {@inheritDoc}
	 * 
	 *
	 */
	@Override
	public void proceed(Date imp_beginn, Date imp_ende) throws IOException,	ContainerException 
	{
		if (filename == null)
			throw new IllegalArgumentException("Pfad zur Import-Datei ist leer.");
		
		if (filename.equals(HTWMStundenplanImport.USE_SEMINARGRUPPE))
			throw new IllegalArgumentException("Wenn sie eine Seminargruppe angeben wollen, muessen sie vorher downloadSeminargruppe() ausfuehren.");

                Iterator I;
                Termin delete;
                Termin tmp;

                Date heute = new Date(System.currentTimeMillis());
                Date ende = new Date(Long.MAX_VALUE);


		//der Klasse den Pfad zur temporaeren Datei mitteilen
		iClass.setFilepath(filename);
                //Quelle und Prioritaet setzen
                iClass.setQuelle(1);
                iClass.setPrioritaet(3);

                //alle zukuenftigen Hochschul Termine loeschen
                delete = new Termin();
                
                delete.setQuelle(1);  //Automatisch hinzugefuegte
                delete.setPrio(3); //Niedrigste Prio

                try {
                    I = ts.tBeginntZwischen(FunctionCollection.convertDatetoString(heute), FunctionCollection.convertDatetoString(ende)).iterator();
                } catch (ParseException ex) {
                    throw new ContainerException(ex);
                }
                
                while (I.hasNext())
                {
                    tmp = (Termin) I.next();
                    ts.tEntfernen(tmp);
                }
               
		//Die Termine importieren
		iClass.proceed(imp_beginn, imp_ende);
	}
	
	/**
	 * Entfernt den Verweis
	 * 
	 * @return Ob das Loeschen erfolgreich war.
	 */
	public boolean removeSeminargruppeVerweis()
	{
            if (iClass.getClass() != ICalender.class)
                throw new IllegalArgumentException("Wird nur unterstuetzt, wenn man als Importtyp iCalender angibt! Typ: " + iClass.getClass().toString());
            
            if (seminargruppe == null)
                    throw new IllegalArgumentException("Geben sie zuerst die Seminargruppe an.");

            java.io.File a = new java.io.File(filename);

            System.out.println("Exists: " + (a.exists()) + " File: " + a.getPath());

            return a.delete();
    }
}
