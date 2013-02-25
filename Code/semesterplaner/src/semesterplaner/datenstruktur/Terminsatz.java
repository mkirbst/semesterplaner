package semesterplaner.datenstruktur;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import semesterplaner.datenstruktur.TerminComparator.TerminAttribut;

/**Klasse Terminsatz
 * Implementiert Datenstruktur und Funktionen zur Verwaltung von Termin-Objekten
 * @author m
 */
public class Terminsatz {

    String datumFormat = "yyyy.MM.dd-HH:mm:ss";	//Jahr.Monat.Tag-Stunde:Minute:Sekunde
    SimpleDateFormat sdf = new SimpleDateFormat(datumFormat);

    /**
     * Variablen für die einzelnen Treesets
     */
    private TreeSet<Termin> tsBez;
    private TreeSet<Termin> tsStt;
    private TreeSet<Termin> tsStp;
    private TreeSet<Termin> tsOrt;
    private TreeSet<Termin> tsVbz;
    private TreeSet<Termin> tsNbz;
    private TreeSet<Termin> tsBem;
    private TreeSet<Termin> tsTyp;
    private TreeSet<Termin> tsPrio;
    private TreeSet<Termin> tsQuelle;

    /**
     * Konstruktor der Klasse Terminsatz
     * Initialisiert die einzelnen Treesets
     */
    public Terminsatz() {							//Konstruktor
        tsBez       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.bez));
        tsStt       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        tsStp       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stp));
        tsOrt       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.ort));
        tsVbz       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.vbz));
        tsNbz       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.nbz));
        tsBem       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.bem));
        tsTyp       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.typ));
        tsPrio      = new TreeSet<Termin>(new TerminComparator(TerminAttribut.prio));
        tsQuelle    = new TreeSet<Termin>(new TerminComparator(TerminAttribut.quelle));
     }

    /**tsAnlegen
     * übernimmt ein TreeSet aus Termin-Objekten und fügt diese in den Terminsatz ein
     * @param ts Treeset das in den Terminsatz eingefügt werden soll
     * @return String "" falls erfolgreich sonst Bezeichner der Attribute die nicht eingefügt werden konnten
     */
    public String tsAnlegen(TreeSet<Termin> ts)  {
        String erg = "";
        Iterator itr = ts.iterator();
        Termin tTemp;

        while(itr.hasNext())  {
            tTemp = (Termin)itr.next();
            erg = this.tAnlegen(tTemp);
        }
        return erg;
    }

    /**tAnlegen
     * fügt ein Terminobjekt in den Terminsatz ein
     * @param t Termin der eingefügt werden soll
     * @return String "" falls erfolgreich, sonst Bezeichner des Attributs das Fehler verursacht hat
     */
    public String tAnlegen(Termin t) {
        String erg = "";    //DEBUG

        if ((t.getBez().trim().equals("")) || (t.getBez()==null) || (t.getStt()==null) || (t.getStp()==null) || (t.getQuelle() < 1) || (t.getPrio() < 1)) {
            throw new IllegalArgumentException("Terminbezeichnung, Startzeitpunkt, Endzeitpunkt, Quelle, Priorität zwingend erforderlich !!");
            //return "FEHLER BEIM EINFÜGEN: Terminbezeichnung, Startzeitpunkt, Endzeitpunkt, Quelle, Priorität zwingend erforderlich";
        } else if (!(t.getStt().getTime() < t.getStp().getTime())) {
            throw new IllegalArgumentException("Startzeitpunkt muss vor Endzeitpunkt liegen !!");
            //return "FEHLER BEIM EINFÜGEN: Startzeitpunkt muss vor Endzeitpunkt liegen !!";
        } else if (!(tsStt.add(t))) {
            throw new IllegalArgumentException("Element schon vorhanden !!");
            //return "FEHLER BEIM EINFÜGEN: Element schon vorhanden !!";
        } else {
            if(!(tsBez.add(t))) erg = erg + "bez ";
            if(!(tsStp.add(t))) erg = erg + "stp ";
            if(!(tsOrt.add(t))) erg = erg + "ort ";
            if(!(tsVbz.add(t))) erg = erg + "vbz ";
            if(!(tsNbz.add(t))) erg = erg + "nbz ";
            if(!(tsBem.add(t))) erg = erg + "bem ";
            if(!(tsTyp.add(t))) erg = erg + "typ ";
            if(!(tsPrio.add(t))) erg = erg + "prio ";
            if(!(tsQuelle.add(t))) erg = erg + "quelle ";
            
        }
        return erg;
    }

    /**tEntfernen
     * Entfernt das übergebene Termin-Objekt aus dem Terminsatz
     * @param t Terminobjekt das entfernt werden soll
     * @return String "" wenn erfolgreich, sonst Bezeichner der Attribute die fehlgeschlagen sind
     */
    public String tEntfernen(Termin t) {
        String erg = "";

        if(!(tsBez.remove(t))) erg = erg + "bez ";
        if(!(tsStt.remove(t))) erg = erg + "sst ";
        if(!(tsStp.remove(t))) erg = erg + "stp ";
        if(!(tsOrt.remove(t))) erg = erg + "ort ";
        if(!(tsVbz.remove(t))) erg = erg + "vbz ";
        if(!(tsNbz.remove(t))) erg = erg + "nbz ";
        if(!(tsBem.remove(t))) erg = erg + "bem ";
        if(!(tsTyp.remove(t))) erg = erg + "typ ";
        if(!(tsPrio.remove(t))) erg = erg + "prio ";
        if(!(tsQuelle.remove(t))) erg = erg + "quelle ";
        return erg;
    }

    /**tAuslesen
     * gibt alle Terminobjekte des Terminsatzes Aus
     * @param ta TerminAttribut nach dem sortiert die Ausgabe erfolgen soll
     */
    public void tAuslesen(TerminAttribut ta)  {
        Iterator itr;

        switch(ta)  {
            case bez:       itr = tsBez.iterator(); break;
            case stt:       itr = tsStt.iterator(); break;
            case stp:       itr = tsStp.iterator(); break;
            case ort:       itr = tsOrt.iterator(); break;
            case vbz:       itr = tsVbz.iterator(); break;
            case nbz:       itr = tsNbz.iterator(); break;
            case bem:       itr = tsBem.iterator(); break;
            case typ:       itr = tsTyp.iterator(); break;
            case prio:      itr = tsPrio.iterator(); break;
            case quelle:    itr = tsQuelle.iterator(); break;
            default:
                throw new IllegalArgumentException("Invalid Enum for Compare");
        }

        while(itr.hasNext())  {
                System.out.println(itr.next().toString());
        }
    }

    /**tKonfliktsuche
     * Durchsucht den Terminsatz nach Terminen die sich zeitlich teilweise oder
     * vollständig überlappen und referenziert diese in einem neuen TreeSet das
     * als Ergebnis zurückgegeben wird
     * @return tsKonflikte() TreeSet mit allen Terminen die in Konflikte verwickelt sind
     */
    public TreeSet<Termin> tKonfliktsuche()  {
        TreeSet<Termin> tsKonflikte = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        Termin t;
        int k = 0;

        Iterator itrn = tsStt.iterator();
        
        while (itrn.hasNext()) {
            t = (Termin)itrn.next();

            Iterator itrm = tsStt.iterator();
            while(itrm.hasNext())  {                // Doppelkonfliktprüfung ausschließen
                if(itrm.next().equals(t)) break;    // 2. Itreator direkt bei t beginnen lassen
            }

            Termin u;
            while(itrm.hasNext())  {
                u = (Termin)itrm.next();
                if(!(t.equals(u)))  {                                               //termine nicht mit sich selbst vergleichen
                    if(t.getStt().getTime() > u.getStt().getTime()) {               //Fälle 1,4,5: Startzeitpunkt t nach Startzeitpunkt u
                        if(!(u.getStp().getTime() < t.getStt().getTime())) {        //Fälle 1,4 Wenn nicht Endzeitpunkt u vor Startzeitpunkt t -> Konflikt
                            k++;
                            //System.out.println("TypI  KONFLIKT "+k+":"+t.getBez()+" "+t.getSttStr()+" "+t.getStpStr()+" "+u.getBez()+u.getSttStr()+" "+u.getStpStr());
                            tsKonflikte.add(t);
                            tsKonflikte.add(u);
                        }
                    } else {                                                        //Startzeit von t ist vor Startzeit von u
                        if(!(t.getStp().getTime() < u.getStt().getTime())) {        //Endet t rechtzeitig bevor u anfängt ??
                            k++;
                            //System.out.println("TypII KONFLIKT "+k+": t:"+t.getBez()+" "+t.getSttStr()+" "+t.getStpStr()+" conflicts with "+u.getBez()+" "+u.getSttStr()+" "+u.getStpStr());
                            tsKonflikte.add(t);
                            tsKonflikte.add(u);
                        }
                    }
                }
            }
        }
        System.out.println("Konfliktsuche über "+ this.size() + " dabei "+ tsKonflikte.size() + " Konfliket gefunden.");
        return tsKonflikte;
    }

    /** tSuche
     * Durchsucht die interne Datenstruktur nach bestimmten Terminattributen
     * Es muss ein Termin-Objekt übergeben werden, das als Attribute die gewünschten Suchkriterien enthält.
     * Es wird im ersten Schritt immer überprüft ob das jeweilige Attribut ein Suchkriterium ist und im zweiten
     * Schritt, ob das jeweilige Suchkriterieum mit dem aktuell überprüften Termin übereinstimmt
     * @param Termin ein Terminobjekt das als Attribuet die Suchkriterien enthält
     * @return Treeset<Termin> das alle Termine enthält die den Suchkriterien entsprechen
     */
    public TreeSet<Termin> tSuche(Termin tSuch) {
        final Termin tLeer = new Termin();  //Vergleichsmuster
        Termin tAkt = new Termin();
        TreeSet<Termin> tsErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        
        Iterator itr = tsStt.iterator();
        while(itr.hasNext()) {
            tAkt = (Termin) itr.next();
            //Alle Attribute durchchecken
            if(!((tLeer.getBez().equals(tSuch.getBez()))))  {  //Ist Bezeichnung Suchkriterieum ?
                if(!(tAkt.getBez().toLowerCase().contains(tSuch.getBez().toLowerCase())))  {   //Kommt Bezeichnung in aktuellem Termin vor ?
                    continue; //Wenn Bezeichner gesetzt, aber nicht übereinstimment, dann ist aktuell untersuchter Termin kein Treffer, weiter bei while
                }
            }
            if((tLeer.getStt().compareTo(tSuch.getStt())) != 0)  {  //Ist Startzeitpunkt ein Suchkriterium ?
                if((tAkt.getStt().compareTo(tSuch.getStt())) != 0) {    //Stimmt Suchkrierium mit aktuellem
                    continue;
                }
            }
            if((tLeer.getStp().compareTo(tSuch.getStp())) != 0)  {
                if((tAkt.getStp().compareTo(tSuch.getStp())) != 0) {
                    continue;
                }
            }
            if(!((tLeer.getOrt().equals(tSuch.getOrt()))))  {  //Ist Ort Suchkriterieum ?
                if(!(tAkt.getOrt().toLowerCase().contains(tSuch.getOrt().toLowerCase())))  {   //Kommt Ort in aktuellem Termin vor ?
                    continue; //Wenn Bezeichner gesetzt, aber nicht übereinstimment, dann ist aktuell untersuchter Termin kein Treffer, weiter bei while
                }
            }
            if(tLeer.getVbz() != tSuch.getVbz())  { //Ist Vorbereitungszeit Suchkriterium ?
                if(tAkt.getVbz() != tSuch.getVbz())  {
                    continue;
                }
            }
            if(tLeer.getNbz() != tSuch.getNbz())  { //Ist Nachbereitungszeit Suchkriterium ?
                if(tAkt.getNbz() != tSuch.getNbz())  {
                    continue;
                }
            }
            if(!((tLeer.getBem().equals(tSuch.getBem()))))  {  //Ist Bemerkung Suchkriterieum ?
                if(!(tAkt.getBem().toLowerCase().contains(tSuch.getBem().toLowerCase())))  {   //Kommt Bemerkung in aktuellem Termin vor ?
                    continue; //Wenn Bemerkung gesetzt, aber nicht übereinstimment, dann ist aktuell untersuchter Termin kein Treffer, weiter bei while
                }
            }
            if(!((tLeer.getTyp().equals(tSuch.getTyp()))))  {  //Ist Typ Suchkriterieum ?
                if(!(tAkt.getTyp().toLowerCase().contains(tSuch.getTyp().toLowerCase())))  {   //Kommt Typ in aktuellem Termin vor ?
                    continue; //Wenn Typ gesetzt, aber nicht übereinstimment, dann ist aktuell untersuchter Termin kein Treffer, weiter bei while
                }
            }
            if(tLeer.getPrio() != tSuch.getPrio())  { //Ist Priorität Suchkriterium ?
                if(tAkt.getPrio() != tSuch.getPrio())  {
                    continue;
                }
            }
            if(tLeer.getQuelle() != tSuch.getQuelle())  { //Ist Quelle Suchkriterium ?
                if(tAkt.getQuelle() != tSuch.getQuelle())  {
                    continue;
                }
            }
            //Stimmen alle Suchkriterien überein, so füge aktuellen Termin dem Ergebnistreeset hinzu.
            tsErg.add(tAkt);
        }
        return tsErg;
    }

    /**tBeginntZwischen
     * Liefert als Ergebnis ein TreeSet mit allen Terminen zurück die zwischen den übergebenen Zeitpunkten beginnen
     * @param start Startzeitpunkt
     * @param ende Endzeitpunkt
     * @return TreeSet mit Terminen 
     * @throws ParseException falls die Zeitpunkte nicht in einem Sring im Format yyyy.MM.dd-HH:mm:ss übergeben werden
     */
    public TreeSet<Termin> tBeginntZwischen(String start, String ende) throws ParseException {
        Termin startzeitpunkt = new Termin();
        startzeitpunkt.setStt(sdf.parse(start));
        Termin endzeitpunkt = new Termin();
        endzeitpunkt.setStt(sdf.parse(ende));
        TreeSet<Termin> tsErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        Iterator itr = tsStt.iterator();
        while(itr.hasNext()) {
            Termin t = (Termin) itr.next();
            if((t.getStt().after(startzeitpunkt.getStt())) && (t.getStt().before(endzeitpunkt.getStt()))) {
                tsErg.add(t);                                                                                //füge ein, wenn zwischen Start/Endzeitpunkt
            }
            if(t.getStt().after(endzeitpunkt.getStt()))  break;             //Sobald Zeitraum abgearbeitet Abbruch
        }

        return tsErg;
    }

    /**tEndetZwischen
     * Liefert als Ergebnis ein TreeSet mit allen Terminen zurück die zwischen den übergebenen Zeitpunkten enden
     * @param start Startzeitpunkt
     * @param ende Endzeitpunkt
     * @return TreeSet mit Terminen
     * @throws ParseException falls die Zeitpunkte nicht in einem Sring im Format yyyy.MM.dd-HH:mm:ss übergeben werden
     */
    public TreeSet<Termin> tEndetZwischen(String start, String ende) throws ParseException {
        Termin startzeitpunkt = new Termin();
        startzeitpunkt.setStp(sdf.parse(start));
        Termin endzeitpunkt = new Termin();
        endzeitpunkt.setStp(sdf.parse(ende));
        TreeSet<Termin> tErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stp));
        Iterator itr = tsStp.iterator();
        while(itr.hasNext()) {
            Termin t = (Termin) itr.next();
            if((t.getStp().after(startzeitpunkt.getStp())) && (t.getStp().before(endzeitpunkt.getStp()))) {
                tErg.add(t);                                                                                //füge ein, wenn zwischen Start/Endzeitpunkt
            }
            if(t.getStp().after(endzeitpunkt.getStp()))  break;             //Sobald Zeitraum abgearbeitet Abbruch
        }

        return tErg;
    }

    /**tBeginntZwischenNonStrict
     * Liefert als Ergebnis ein TreeSet mit allen Terminen zurück die zwischen oder auf den übergebenen Zeitpunkten beginnen
     * @param start Startzeitpunkt
     * @param ende Endzeitpunkt
     * @return TreeSet mit Terminen
     * @throws ParseException falls die Zeitpunkte nicht in einem Sring im Format yyyy.MM.dd-HH:mm:ss übergeben werden
     */
    public TreeSet<Termin> tBeginntZwischenNonStrict(String start, String ende) throws ParseException {
        Termin startzeitpunkt = new Termin();
        startzeitpunkt.setStt(sdf.parse(start));
        Termin endzeitpunkt = new Termin();
        endzeitpunkt.setStt(sdf.parse(ende));
        TreeSet<Termin> tsErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        Iterator itr = tsStt.iterator();
        while(itr.hasNext()) {
            Termin t = (Termin) itr.next();
            if((t.getStt().compareTo(startzeitpunkt.getStt()) >= 0 && t.getStt().compareTo(endzeitpunkt.getStt()) <= 0 )) {
                tsErg.add(t);                                                                                //füge ein, wenn zwischen Start/Endzeitpunkt
            }
            if(t.getStt().after(endzeitpunkt.getStt()))  break;             //Sobald Zeitraum abgearbeitet Abbruch
        }

        return tsErg;
    }

    /**tEndetZwischenNonStrict
     * Liefert als Ergebnis ein TreeSet mit allen Terminen zurück die zwischen oder auf den übergebenen Zeitpunkten enden
     * @param start Startzeitpunkt
     * @param ende Endzeitpunkt
     * @return TreeSet mit Terminen
     * @throws ParseException falls die Zeitpunkte nicht in einem Sring im Format yyyy.MM.dd-HH:mm:ss übergeben werden
     */
    public TreeSet<Termin> tEndetZwischenNonStrict(String start, String ende) throws ParseException {
        Termin startzeitpunkt = new Termin();
        startzeitpunkt.setStp(sdf.parse(start));
        Termin endzeitpunkt = new Termin();
        endzeitpunkt.setStp(sdf.parse(ende));
        TreeSet<Termin> tErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        Iterator itr = tsStp.iterator();
        while(itr.hasNext()) {
            Termin t = (Termin) itr.next();
            if((t.getStp().compareTo(startzeitpunkt.getStp()) >= 0) && t.getStp().compareTo(endzeitpunkt.getStp()) <= 0) {
                tErg.add(t);                                                                              //füge ein, wenn zwischen Start/Endzeitpunkt
            }
            if(t.getStp().after(endzeitpunkt.getStp()))  break;             //Sobald Zeitraum abgearbeitet Abbruch
        }

        return tErg;
    }

    /**gettsBez
     * Liefert eine Referenz auf das TreeSet im Terminsatz, dass nach Terminbezeichnern sortiert aufgebaut ist
     * @return TreeSet sortiert nach Terminbezeichnern
     */
    public TreeSet<Termin> gettsBez(){
        return tsBez;
    }

    /**size
     * Liefert die Anzahl der Terminobjekte im Terminsatz zurück
     * @return Anzahl Termine
     */
    public int size(){
        return tsStt.size();
    }

    /**clear
     * Reinitialisiert den Terminsatz
     */
    public void clear() {
        tsBez       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.bez));
        tsStt       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
        tsStp       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stp));
        tsOrt       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.ort));
        tsVbz       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.vbz));
        tsNbz       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.nbz));
        tsBem       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.bem));
        tsTyp       = new TreeSet<Termin>(new TerminComparator(TerminAttribut.typ));
        tsPrio      = new TreeSet<Termin>(new TerminComparator(TerminAttribut.prio));
        tsQuelle    = new TreeSet<Termin>(new TerminComparator(TerminAttribut.quelle));
    }
}
