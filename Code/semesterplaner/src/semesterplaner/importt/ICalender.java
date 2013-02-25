package semesterplaner.importt;

import java.io.*;

import java.util.Iterator;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;

import semesterplaner.exceptions.ContainerException;
import semesterplaner.libs.*;
import semesterplaner.datenstruktur.*;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Comment;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.Summary;
import semesterplaner.libs.iCalender.SMPlaneriCalTypes;

/**
 * Mit dieser Klasse kann man iCalender Dateien in einen Terminsatz importieren.
 * Dazu muss man den Konstrukor mit einen Termin aufrufen, den Pfad setzen, und die Methode "proceed()" ausfuehren.
 * 
 * @author Christian Vorberg
 */
public class ICalender extends ImportClass
{
        public String encoding = "UTF-8";

	/**
	 * Setzt einen Terminsatz in den die erhaltenen Termine gespeichert werden sollen.
	 * @param ts 
	 */
	public ICalender(Terminsatz ts)
	{
		super(ts);
	}

        public ICalender(Terminsatz ts,String encoding)
	{
		super(ts);
                this.encoding = encoding;
	}



	/**
	 * Importiert die iCalender Daten in den Terminsatz.
	 * 
	 * {@inheritDoc}
	 * 
	 * @throws ParserException (Inhalt von ContainerException) Wird ausgeloest, wenn die uebergebene iCalender Datei Fehler enthalt.
         * @todo Kommentare importieren
	 */
	public void proceed(java.util.Date imp_beginn, java.util.Date imp_ende) throws IOException, ContainerException
	{
         
            
			//Ein Input Stream, der Leerzeilen entfernt anlegen
			Reader fin = new RemoveEmptyLinesInputReader(new BufferedReader(new FileReader(filename)));
			CalendarBuilder builder = new CalendarBuilder(); //Der Parser
			Calendar calendar; //Das Entgueltige iCalender Objekt

                        TimeTaker tt = new TimeTaker();

			try {
                                if (Config.DEBUG)
                                {
                                    System.err.println("DEBUG: Parse iCalender... (iCal4j!)");
                                    tt.startTime();
                                }
				calendar = builder.build(fin); //Parsen
                                
                                if (Config.DEBUG)
                                {
                                    tt.endTime();
                                    System.err.println("Took iCal4j: " + tt.toString());
                                }
				fin.close(); //InputStream Schliessen
			} catch (ParserException e1) {
				throw new ContainerException(e1); //Aufgetretene Exceptions weiterwerfen!
			}

			if (Config.DEBUG)
                        {
                            System.err.println("DEBUG: Put in Calender Termine...");
                            tt.startTime();
                        }


			VEvent v; //VEvent ist EIN Termin
			Termin t = null;
		
			/** Spezielle Datentypen von iCal4j **/
			Summary summary; //Bezeichnung
			DtStart beginn; //Termin-Beginn
			DtEnd end; //Termin-Ende
			Location ort; //Termin-Ort
                        Priority prio; //Prioritaet
			
			/** Die End-Datentypen mit den gearbeitet wird.  **/
			String real_summary = null; //Bezeichnung
                        java.util.Date real_beginn; //Termin-Beginn
                        java.util.Date real_end; //Termin-Ende
                        String real_ort = null; //Termin-Ort
                        int real_prio; //Termin-Priorität
                        String real_comm; //Termin Kommentar
                        // Folgende Typen sind Extra-Typen //
                        int real_quelle; //Termin Quelle
                        int real_vbz; //Vorbereitungszeit
                        int real_nbz; //Nachbereitungszeit
                        String real_termintyp; //Termintyp
                        

		    
			//Durch alle VEvents der ICalender Datei durchiterieren.
			for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) 
			{
			    Component component = (Component) i.next(); //den Naechsten Termin holen



			    //Alles was kein Termin ist, brauchen wir nicht
			    if (!component.getName().equals(Component.VEVENT))
                            {
			    	continue;
                            }
			    		    
			    v = new VEvent(component.getProperties()); //Den Inhalt des Termins (VEvent) holen

			    summary = v.getSummary();
			    beginn = v.getStartDate();		    
			    end = v.getEndDate();
			    ort = v.getLocation();
                            prio = v.getPriority();

                            //Folgende Propertys werden ueber Funktionen abgehandelt, deshalb: real-zugriff
                            real_comm = getVEValue(v,Property.COMMENT);
                            real_quelle = getVEValue(v,SMPlaneriCalTypes.QUELLE,v_quelle);
                            real_vbz = getVEValue(v,SMPlaneriCalTypes.VORBEREITUNGSZEIT,0);
                            real_nbz = getVEValue(v,SMPlaneriCalTypes.NACHBEREITUNGSZEIT,0);
                            real_termintyp = getVEValue(v,SMPlaneriCalTypes.TERMINTYP);

                             
			    /** Fuer alle darunterliegenden Attribute gilt: Werden nur genutzt, wenn sie nicht null sind.
			     *  Der Beginn und das Ende eines Termins muessen immer vorhanden sein.
			     */

                            //Zeitzone loeschen, ansonsten koennen wir nicht serialisieren!
                            beginn.setTimeZone(null);
                            end.setTimeZone(null);

			    
			    //Termine die kein Anfangsdatum haben nuetzen uns nichts!
			    if (beginn != null) real_beginn = beginn.getDate(); else continue;
			    //Wenn wir ab einen bestimmten Zeitpunkt importieren wollen, werden alle die darunterliegen weggeschmissen
			    if (imp_beginn != null && real_beginn.compareTo(imp_beginn) < 0) continue;
			    //Termine die kein Enddatum haben nuetzen uns nichts!
			    if (end != null) real_end = end.getDate(); else continue;
			    //Wenn wir bis zu einen bestimmten Zeitpunkt importieren wollen, werden alle die darueberliegen weggeschmissen
			    if (imp_ende != null && real_end.compareTo(imp_ende) > 0) continue;
			    //Bezeichnung setzen
			    if (summary != null) real_summary = summary.getValue(); 
			    //Ort setzen, ansonsten: "Unbekannt". (@TODO steht noch zur diskussion)
			    if (ort != null) real_ort = ort.getValue(); else real_ort = "[unbekannt]";
                            //Prioritaet setzen, ansonsten standart-prioritaet.
                            if (prio != null) real_prio = prio.getLevel(); else real_prio = v_prioritaet;

                            
			    try
			    {
			    	//Termin mit diesen Attributen erstellen
			    	t = new Termin();
			    	t.setStt(real_beginn);
			    	t.setStp(real_end);
			    	t.setBez(real_summary);
			    	t.setOrt(real_ort);
                                t.setBem(real_comm);
                                t.setPrio(real_prio);
                                t.setQuelle(real_quelle);
                                t.setVbz(real_vbz);
                                t.setVbz(real_nbz);
                                t.setTyp(real_termintyp);
			    } catch (IllegalArgumentException e)
			    {
			    	System.out.println("Terminbeginn oder Terminende ist null. " + e.getMessage());
			    	System.out.println("Beschreibung: " + real_summary);
			    	System.out.println("Termin NICHT hinzugefuegt!");
			    	continue;
			    
			    }
			    //Termin in Terminsatz einfuegen
                            try
                            {
                                ts.tAnlegen(t);
                            } catch (IllegalArgumentException e)
                            {
                                if (!e.getMessage().equals("Element schon vorhanden !!"))
                                    throw e;
                                // ?? else if (Config.DEBUG) ?? 
                            }

			}
                        
                        if (Config.DEBUG)
                        {
                            tt.endTime();
                            System.err.println("Took input into Terminsatz: " + tt.toString());
                        }

	}

        /**
         * Hohlt einen ganzzahligen Wert aus einen Event. Wenn die Property nicht vorhanden ist, wird stdwert genommen.
         * @param v Event aus dem die Property geholt wird
         * @param property Property die Gesucht wird
         * @param stdwert Wert der benutzt wird, wenn die Property nicht gefunden wird.
         * @return
         */
        private int getVEValue(VEvent v, String property, int stdwert)
        {
            Property p = v.getProperty(property);
            if (p == null)
                return stdwert;
            else
            {
                int ret;
                 try{
                    ret = Integer.parseInt(p.getValue());
                 } catch (NumberFormatException e) {
                    ret = stdwert;
                 }
                return stdwert;
            }
        }

        /**
         * Hohlt einen String aus einen Event. Wenn die Property nicht vorhanden ist, wird ein leerer String genommen.
         * @param v Event aus dem die Property geholt wird
         * @param property Property die Gesucht wird
         * @return
         */
        private String getVEValue(VEvent v, String property)
        {
            Property p = v.getProperty(property);
            if (p == null)
                return "";
            else
                return p.getValue();
        }
}
