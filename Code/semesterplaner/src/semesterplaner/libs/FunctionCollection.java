package semesterplaner.libs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import semesterplaner.datenstruktur.*;

/**
 * Einige gesammelte Funktionen.
 */
public final class FunctionCollection 
{
	/**
	 * Diese Funktion ueberprueft ob 2 Objekte gleich sind, nach bestimmten Regeln:
	 * 
	 * - Wenn der "Test" Parameter Null ist, sind sie gleich
	 * - Wenn es beide Strings sind, und der Org String einen Teil von Test enthaelt, sind sie gleich
	 * - Alle anderen Datentypen: wenn sie equals-gleich sind, sind sie gleich. 
	 * 
	 * Es ist zu beachten, dass beide Objekte die gleiche Klasse sein muessen.
	 * 
	 * (Vereinfachungsfunktion fuer "getTermin()")
	 * 
	 * @param org Das Originale Objekt
	 * @param test Das Testobjekt, mit dem ueberprueft werden soll
	 * @return Ob beide Objekte gleich sind. bzw. liefert true, wenn test null ist.
	 * @throws IllegalArgumentException, wenn org null ist , und wenn beide uebergebenen Objekte nicht die selben Klassen beinhalten. 
	 * @see semesterplaner.main.Terminsatz (Funktion getTermin())
	 * @todo Funktion Testen (String-vergleich) und die Benutzung von getClass() wurde nur auf verdacht angewendet, das es so Funktioniert!
	 * @author Christian Vorberg
	 */
	public static boolean equalsExtended(Object org, Object test) throws IllegalArgumentException
	{
		if (org == null)
			throw new IllegalArgumentException("Parameter org ist null");
		if (test == null)
			return true;
		
		//Ueberprueft ob die Orginalklasse gleich der Testklasse ist. (Instanceof funktioniert nicht, bevor ihr euch beschwert!)
		if (org.getClass().equals(test.getClass()))
			throw new IllegalArgumentException("Die beiden uebergebenen Objekte beinhalten nicht die selbe Klasse.");	
		
		//2 Strings
		if (org instanceof String)
		{
			String o = (String) org;
			String t = (String) test;
			
			//Enthaelt der String den Substring? 
			return o.contains(t.subSequence(0, t.length()-1)); //Subsequence, weil o.contains() eine CharSequence will
		}
		else 
			return org.equals(test);
	}
	/**
	 * Gibt true zureuck, wenn beide Objecte null sind, oder beide equalsgleich sind.
	 * 
	 * @param o Das ueberpruefende Object
	 * @param t Das Objekt mit dem ueberpreuft werden soll
	 * @return Ob beide gleich sind, wenn sie beide null oder equalsgleich sind.
	 */
	public static boolean equalsNull(Object o, Object t) 
	{
		if (o == null && t == null)
			return true;
		else if (o == null || t == null)
			return false;
		else if (o.equals(t))
			return true;
		else
			return false;
	}

        /**
         * Konvertiert ein Date-Objekt zu einen String entsprechend der angabe in Termin.datumFormat.
         * @param date Der String der Konvertiert werden soll
         * @return Einen String in der Form von Termin.datumFormat
         * @see Termin.datumFormat
         */
        public static String convertDatetoString(Date date)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(Termin.datumFormat);
            return sdf.format(date);
        }

        /**
         * Fuegt zu einen Vector die Werte aus einen Array hinzu
         * @param vec Der Vector zu dem die Werte hinzugefuegt werden sollen
         * @param values Die Werte die hinzugefuegt werden sollen
         */
        public static void addToVector(Vector<String> vec, String[] values)
        {
            for (int I = 0; I < values.length ; I++)
                vec.add(values[I]);
        }

        /**
         * Erzeugt aus einen String-Array einen Vector.
         * @param values Die Werte
         * @return Vector bestehend aus den Werten von values.
         */
        public static Vector<String> createStringVector(String[] values)
        {
            Vector<String> av = new Vector<String>(values.length);
            addToVector(av,values);
            return av;

        }

        /**
         * Erstellt ein Array welches die Werte von from bis to enthaelt.
         * @param from
         * @param to
         * @return Array mit den durchgehenden Zahlen von from bis to. Groesse: to-from.
         */
        public static int[] getArrayWithNumbers(int from, int to)
        {
            int[] array = new int[to-from];

            int zahl = from;
            for (int I = 1 ; I <= to-from ; I++)
            {
                array[I-1] = zahl;
                zahl++;
            }
            return array;
        }

}

