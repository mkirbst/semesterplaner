/*
 *Hilfsklasse zur Berechnung der Gesamtzeit
 *
 */

package semesterplaner.datenstruktur;

/**
 *
 * @author Sascha Lissek
 */



public class ZeitObjekt {

    private String sBez="";
    private long lVorlesungZeit, lGesamtZeit = 0;
    private int iVorNachZeit = 0;

    public ZeitObjekt(String bez, long vorzeit, int vornachzeit){
        sBez=bez;
        lVorlesungZeit=vorzeit;
        iVorNachZeit=vornachzeit;
        lGesamtZeit=lVorlesungZeit+iVorNachZeit*60000;
    }

    public String getBezeichner(){
        return sBez;
    }

    public long getVorlesungZeit(){
        return lVorlesungZeit;
    }

    public int getVorNachZeit(){
        return iVorNachZeit;
    }

    public long getGesamtZeit(){
        return lGesamtZeit;
    }

    public void setBezeichner(String bez){
        sBez=bez;
    }

    public void setVorlesungZeit(long vorzeit){
        lVorlesungZeit=vorzeit;
    }

    public void setVorNachZeit(int vornach){
        iVorNachZeit=vornach;
    }

    public void setGesamtZeit(long gesamt){
        lGesamtZeit=gesamt;
    }





}
