package semesterplaner.main;

import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TreeSet;

import semesterplaner.libs.TimeTaker;
import semesterplaner.datenstruktur.*;

import semesterplaner.exceptions.ContainerException;
import semesterplaner.export.BinaryEx;
import semesterplaner.importt.BinaryIm;
import semesterplaner.importt.HTWMStundenplanImport;
import semesterplaner.libs.FunctionCollection;

/**
 * Eine Klasse zum Testen des HTWM-Imports.
 * @author Christian Vorberg
 */
public class HTWMStundenplanTest 
{
	public static void main(String[] args) throws Exception
	{
                semesterplaner.export.ICalender icalex = new semesterplaner.export.ICalender("iCalendar.ics");
                SimpleDateFormat sdf = new SimpleDateFormat(Termin.datumFormat);
                
		TimeTaker time = new TimeTaker();
		BufferedReader inp = new BufferedReader( new InputStreamReader( System.in ) );
		String temp;
		boolean download;
		
		Terminsatz t = new Terminsatz();
		Date a = new Date(System.currentTimeMillis());
		Date b = new Date(a.getTime() + 604800000);
		HTWMStundenplanImport im = new HTWMStundenplanImport(t,HTWMStundenplanImport.IM_ICAL);


                //sollte er loeschen
                Termin neuTermin = new Termin();
                neuTermin.setBez("Loesche Mich!");                           //neues Terminobjekt anlegen
                neuTermin.setStt(sdf.parse("1012.1.1-22:00:00"));				//Startzeitpunkt des neuen Termin setzen
                neuTermin.setStp(sdf.parse("2012.1.1-23:30:00"));				//Endzeitpunkt des neuen Termin setzen
                neuTermin.setOrt(":-P");
                neuTermin.setQuelle(2); // 1 = automatisch 2 = manuell hinzugefügt
                neuTermin.setPrio(1);
                t.tAnlegen(neuTermin);

		System.out.print("Seminargruppe angeben: ");
		temp = inp.readLine();

		im.setSeminargruppe(temp);

		System.out.print("Maximalen Zeitraum Importieren? (Ansonsten Beginn + 1 Woche), (J/alles andere: nein): ");
		temp = inp.readLine();
               
                /******** Beispiel ICal Download ***********/
		System.out.print("Download...");
		download = im.downloadSeminargruppe();           
                 
		if (!download)
		{
			System.out.println(" Failed.");
			im.removeSeminargruppeVerweis();
			return;
		}
		else
			System.out.println(" Ready.");

		try {
			if (temp.toUpperCase().equals("J"))
				b = null;
			
			System.out.println("Import: " + a.toString() + " - " + (b == null ? "END" : b.toString() ));
			System.out.print("Import...");
			time.startTime();
		/******** Beispiel ICal Import ***********/
                        im.proceed(a,b);
			time.endTime();
			System.out.println(" Ready. It took: " +  time.toString());
		} catch (ContainerException e) {
			throw e.getException();
		}
		
		im.removeSeminargruppeVerweis();
                
                System.out.println(t.size());
                t.tAuslesen(TerminComparator.TerminAttribut.stt);

                /******** Beispiel ICal Export ***********/
                System.out.print("Schreibe Terminsatz in eine iCalender Datei... ");
                icalex.proceed(t.tSuche(new Termin())); //alles exportieren

                System.out.println("Fertig.");

               /* System.out.println("Gebe alle Termine aus ohne Suchfunktion:");
                t.tAuslesen(TerminComparator.TerminAttribut.stt);
                System.out.println("Gebe alle Termine aus mittels Suchfunktion:");
                Iterator I = t.tSuche(new Termin()).iterator();
                while (I.hasNext())
                {
                    System.out.println(I.next());
                }



                /******** Beispiel Binär Im/Export ***********/
                System.out.println("Aktuelle Woche in eine Binaerdatei schreiben und in einen NEUEN Terminsatz einfuegen:");
                Terminsatz binaersatz = new Terminsatz();
                Date jetzt = new Date(System.currentTimeMillis());
		Date jetzt_plus_eine_woche = new Date(a.getTime() + 604800000);

                String fp = "BINARYTMPFILE.saf";
                BinaryEx bex = new BinaryEx(fp);
                BinaryIm bei = new BinaryIm(binaersatz);

                TreeSet<Termin> extreeset = t.tBeginntZwischen(FunctionCollection.convertDatetoString(jetzt),
                                            FunctionCollection.convertDatetoString(jetzt_plus_eine_woche));
                bex.proceed(extreeset);
                
                bei.setFilepath(fp);
                bei.proceed(null, null); //alles importieren

                binaersatz.tAuslesen(TerminComparator.TerminAttribut.stt);

                java.io.File binfile = new java.io.File(fp);
                binfile.delete();
	}
}
