package semesterplaner.importt;

import java.io.IOException;
import java.util.Date;

import semesterplaner.exceptions.ContainerException;
import semesterplaner.datenstruktur.*;

/**
 * Eine Abstrakte Klasse, die als Vorfahr fuer alle Importklassen verwendet werden soll.
 * 
 * @author Christian Vorberg
 */
public abstract class ImportClass 
{
	/**
	 * Der Terminsatz in den Importiert werden soll
	 */
	protected Terminsatz ts;
	/**
	 * Der Pfad zur zu-Importierenden Datei
	 */
	protected String filename = null;
	/**
	 * Die Quelle die fuer diesen Vorgang gesetzt werden soll.
	 * Standartmaesig: Manuell.
	 */
	protected int v_quelle = 2;
	/**
	 * Die Prioritaet die fuer diesen Vorgang gesetzt werden soll.
         * (Wenn keine Prioritaet vorhanden ist!)
	 * Standartmaesig: 1 (Niedrigste)
	 */
	protected int v_prioritaet = 3;
	
	/**
	 * Erzeugt die Import-Klasse. Muss einen gueltigen Terminsatz uebergeben.
	 * @param ts
	 */
	public ImportClass(Terminsatz ts)
	{
		this.ts = ts;
		filename = null;
	}
	
	/**
	 * Setzt den Pfad zur Datei, die die Termine enthaelt.
	 * 
	 * @param path
	 */
	public void setFilepath(String path)
	{
		this.filename = path;
	}

        /**
	 * Holt den Pfad zur Datei, die die Termine enthaelt.
	 */
	public String getFilepath()
	{
		return this.filename;
	}
	
	/**
	 * Loest den Importiervorgang aus, allerdings nur zwischen 2 bestimmten Zeitpunkten.
	 * (Inkl. den Zeitpunkten selbst, heisst genau diese Zeitpunkte werden auch mit importiert.)
	 * 
	 * @param imp_beginn Wenn NICHT null, wird ab diesen Zeitpunkt importiert. (Ansonsten von Anfang an!)  
	 * @param imp_ende Wenn NICHT null, wird bis zu diesen Zeitpunkt importert. (Ansonsten bis zum Ende!)
	 * @throws IOException Bei I/O Fehlern.
	 * @throws ContainerException Die Funktion sammelt alle Funktionen auf, die sie von ihren Unter-Funktionen bekommen hat, und wirft diese weiter. (Wird gemacht, um z.B. die "ParserException" von iCal4j weiterzuwerfen!)
	 * @throws IllegalArgumentException Wenn kein Pfad zur Datei mittels setFilepath() angegeben wurde.
	 */
	public abstract void proceed(Date imp_beginn, Date imp_ende) throws IOException,ContainerException;
	
	/**
	 * Loest den Importierungsvorgang ueber den gesamten Zeitraum der Importierten Datei.
	 * @throws IOException Bei I/O Fehlern.
	 * @throws ContainerException Die Funktion sammelt alle Funktionen auf, die sie von ihren Unter-Funktionen bekommen hat, und wirft diese weiter. (Wird gemacht, um z.B. die "ParserException" von iCal4j weiterzuwerfen!)
	 */
	public void proceed() throws IOException,ContainerException
	{
		proceed(null,null);
	}
	
	/**
	 * Setzt die Quelle fuer diesen Vorgang.
	 * 
	 * Quelle darf nicht init sein.
	 * 
	 * @param quelle
	 */
	public void setQuelle(int q)
	{
		if (q == 0)
			throw new IllegalArgumentException("Quelle darf nicht Init sein!");
		
		v_quelle = q;
	}
	
	/**
	 * Setzt die Prioritaet fuer diesen Vorgang.
	 * 
	 * Prioritaet muss >= 1 sein.
	 * 
	 * @param quelle
	 */
	public void setPrioritaet(int prio)
	{
		if (prio < 1)
			throw new IllegalArgumentException("Prioritaet ist kleiner 0!");
		v_prioritaet = prio;
	}
}
