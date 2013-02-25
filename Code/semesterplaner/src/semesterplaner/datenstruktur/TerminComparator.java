package semesterplaner.datenstruktur;

import java.io.Serializable;
import java.util.Comparator;

public class TerminComparator implements Comparator, Serializable {
    public enum TerminAttribut { bez, stt, stp, ort, vbz, nbz, bem, typ, prio, quelle };

    private TerminAttribut ta;

    private TerminComparator() {}   //Default-Comprator deaktivieren

    /**TerminComparator
     * Erlaubt es Termine mit gleichen Attributen in ein TreeSet einzusortieren,
     * obwohl das eigentlich nicht erlaubt ist. Dieses wird erreicht indem solange
     * weitere Attribute verglichen werden bis entweder unterschiedle Attribute
     * gefunden werden oder der exakte Termin schon so in dem Terminsatz vorhanden ist
     *
     * @param ta Terminattribut
     */
    public TerminComparator(TerminAttribut ta)
    {
        this.ta = ta;
    }

	/**
	 * Führt eine Zweitsortierung durch, falls der Hauptvergleich identisch
	 * ergibt
	 * @param t1 Termin 1
	 * @param t2 Termin 2
	 * @return 1 wenn zweitvergleich größer oder gleich ergibt, -1 sonst
	 */
	private int secondaryCompare(Termin t1, Termin t2)
	{
		//startzp, endzt, bezeichnung, priorität, quelle
		long dif = t1.getStt().getTime() - t2.getStt().getTime();
		if (dif < 0) return -1;
		else if (dif > 0) return 1;

		dif = t1.getStp().getTime() - t2.getStp().getTime();
		if (dif < 0) return -1;
		else if (dif > 0) return 1;

		int res = t1.getBez().compareToIgnoreCase(t2.getBez());
		if (res != 0) return res;

		res = t1.getPrio() - t2.getPrio();
		if (res != 0) return res;

		res = t1.getQuelle() - t2.getQuelle();
		if (res != 0) return res;

		res = t1.getOrt().compareToIgnoreCase(t2.getOrt());
		if (res != 0) return res;

		res = t1.getVbz() - t2.getVbz();
		if (res != 0) return res;

		res = t1.getNbz() - t2.getNbz();
		if (res != 0) return res;

		res = t1.getBem().compareToIgnoreCase(t2.getBem());
		if (res != 0) return res;

		res = t1.getTyp().compareToIgnoreCase(t2.getTyp());
		if (res != 0) return res;

		// Alles gleich, Pech gehabt :)
		return 0;
	}

        /**Vergleicht zwei Termin-Objekte
         * @param o1 Terminobjekt 1
         * @param o2 Terminobjekt 2
         * @return int dessen Vorzeichen je nachdem welcher Termin bevorzugt wird, varriert
         */
        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null)
                throw new NullPointerException("Uebergebenes Objekt ist nicht initialisisiert.");
            if (!(o1 instanceof Termin) || !(o2 instanceof Termin))
                throw new IllegalArgumentException("Uebergebenes Objekt ist nicht vergleichbar.");

            Termin t1 = (Termin)o1;
            Termin t2 = (Termin)o2;

            // Folgendes Problem trat bei der alten Implementierung auf:
                    // Ein Custom-Comperator darf nur 0 zurückgeben, wenn das Object 100%
                    // identisch ist. Wenn aber Priorität bei 2 Terminen 3 ist wurde immer
                    // 0 zurückgegeben, dadurch dachte das TreeSet, dass die Objekte gleich
                    // sind und hat daher das 2. verworfen.
                    // Korrekt ist die folgende Lösung: Zuerst prüfen wir auf equalsgleich
                    // wenn das zutrifft, ist das Objekt wirklich gleich und wird verworfen.
                    // Wenn das objekt nicht equalsgleich ist, ist es nicht identisch und
                    // alle weiteren Vergleiche, wo ein Wert gleich ist, müssen 1 zurück-
                    // geben.

            long dif = 0;
                    int res = 0;
            switch (ta)         //nach welchem Attribut soll sortiert werden ?
            {
                case bez:
                    res = t1.getBez().compareToIgnoreCase(t2.getBez());
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case stt:
                    dif = t1.getStt().getTime() - t2.getStt().getTime();
                    if (dif < 0) return -1;
                    else if (dif > 0) return 1;
                                    else return secondaryCompare(t1, t2);
                case stp:
                    dif = t1.getStp().getTime() - t2.getStp().getTime();
                    if (dif < 0) return -1;
                    else if (dif > 0) return 1;
                                    else return secondaryCompare(t1, t2);
                case ort:
                    res = t1.getOrt().compareToIgnoreCase(t2.getOrt());
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case vbz:
                    res = t1.getVbz() - t2.getVbz();
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case nbz:
                    res = t1.getNbz() - t2.getNbz();
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case bem:
                    res = t1.getBem().compareToIgnoreCase(t2.getBem());
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case typ:
                    res = t1.getTyp().compareToIgnoreCase(t2.getTyp());
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case prio:
                    res = t1.getPrio() - t2.getPrio();
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                case quelle:
                    res = t1.getQuelle() - t2.getQuelle();
                                    if (res == 0) return secondaryCompare(t1, t2);
                                    else return res;
                default:
                    throw new IllegalArgumentException("Invalid Enum for Compare");
        }
    }
}