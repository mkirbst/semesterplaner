/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.export;

import java.io.*;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Comment;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.Dates;
import net.fortuna.ical4j.util.TimeZones;

import semesterplaner.exceptions.ContainerException;
import semesterplaner.datenstruktur.Termin;
import semesterplaner.libs.iCalender.*;


/**
 * ICalender Export
 * @author Christian Vorberg
 */
public class ICalender extends ExportClass
{
    /**
     * Erzeugt den iCalender-Export
     * @param path Der Pfad wo die Datei gespeichert werden soll.
     */
    public ICalender(String path)
    {
        super(path);
    }


    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException Wenn der Pfad oder der Treeset null ist.
     */
    @Override
    public void proceed(TreeSet<Termin> treeset) throws IOException,ContainerException
    {
        if (this.filename == null)
            throw new IllegalArgumentException("Dateipfad zur Importierenden Datei ist leer!");

        if (treeset == null)
            throw new IllegalArgumentException("Das Treeset darf nicht null sein!");

        FileWriter fw = new FileWriter(this.filename);
        Iterator I;
        Termin st; //Semesterplanertermin

        Calendar calendar; //iCalender
        VEvent ct; //iCalender Termin
        Summary summary; //Bezeichnung
	DtStart beginn; //Termin-Beginn
	DtEnd end; //Termin-Ende
	Location ort; //Termin-Ort
        Priority prio; //Prioritaet
        Comment comm; //Kommentar
        StringProperty quelle; //Quelle
        StringProperty nbz; //Nachbereitungszeit
        StringProperty vbz; //Vorbereitungszeit
        StringProperty termintyp; //Termintyp





        //Calender anlegen und Header erstellen
        calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Semesterplaner//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        I = treeset.iterator();
        while (I.hasNext())
        {
            st = (Termin) I.next();
            
            //Die Properties anlegen
            summary = new Summary(st.getBez());
            beginn = new DtStart(new DateTime(st.getStt()));
            end = new DtEnd(new DateTime(st.getStp()));
            ort = new Location(st.getOrt());
            prio = new Priority(st.getPrio());
            comm = new Comment(st.getBem());
            quelle = new StringProperty(SMPlaneriCalTypes.QUELLE,st.getQuelle().toString());
            vbz = new StringProperty(SMPlaneriCalTypes.VORBEREITUNGSZEIT,st.getVbz().toString());
            nbz = new StringProperty(SMPlaneriCalTypes.NACHBEREITUNGSZEIT,st.getNbz().toString());
            termintyp = new StringProperty(SMPlaneriCalTypes.TERMINTYP,st.getTyp());


            
            //Zum VEvent hinzuguegen
            ct = new VEvent();
            ct.getProperties().add(summary);
            ct.getProperties().add(beginn);
            ct.getProperties().add(end);
            ct.getProperties().add(ort);
            ct.getProperties().add(prio);
            ct.getProperties().add(comm);
            ct.getProperties().add(quelle);
            ct.getProperties().add(vbz);
            ct.getProperties().add(nbz);
            ct.getProperties().add(termintyp);

            //Event zum iCalender hinzufügen
            calendar.getComponents().add(ct);
        }

        //Ausgeben
        fw.write(calendar.toString());
        fw.close();
    }
    
}


