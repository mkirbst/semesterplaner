package semesterplaner.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import semesterplaner.datenstruktur.*;
import semesterplaner.importt.BinaryIm;
import semesterplaner.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import semesterplaner.datenstruktur.TerminComparator.TerminAttribut;
import semesterplaner.gui.*;
import java.text.*;

/**
 *  Klasse zum Testen einzelner Teilfunktionen und von Objekten
 * @author Worg
 */
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
    }
    private Terminsatz testTerminsatz;          //Testterminsatz
    private Termin testTermin1;
    private Termin testTermin2;
    private Termin testTermin3;
    private Termin testTermin4;
    String datumFormat = "yyyy.MM.dd-HH:mm:ss";	//Jahr.Monat.Tag-Stunde:Minute:Sekunde
    SimpleDateFormat sdf = new SimpleDateFormat(datumFormat);


    /**
     * Klasse zum setzen der Ausgangsbedingungen
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        testTerminsatz = new Terminsatz();
        System.out.println("Terminsatz erstellt");

        testTermin1 = new Termin();
        testTermin1.setBez("Vorlesung Softwaretechnik");                           //neues Terminobjekt anlegen
        testTermin1.setStt(sdf.parse("2011.02.15-09:45:00"));				//Startzeitpunkt des neuen Termin setzen
        testTermin1.setStp(sdf.parse("2011.02.15-11:15:00"));				//Endzeitpunkt des neuen Termin setzen
        testTermin1.setQuelle(1); // 1 = automatisch 2 = manuell hinzugefügt
        testTermin1.setPrio(10);
        testTermin1.setVbz(0);
        testTermin1.setNbz(30);
        testTermin1.setOrt("8-203");
        testTermin1.setTyp("Vorlesung");
        testTermin1.setBem("Schreibzeug mitnehmen");
        testTerminsatz.tAnlegen(testTermin1);
        System.out.println("Termin 1 eingefügt");

        testTermin2 = new Termin();
        testTermin2.setBez("Seminar");                           //neues Terminobjekt anlegen
        testTermin2.setStt(sdf.parse("2011.02.15-09:15:00"));				//Startzeitpunkt des neuen Termin setzen
        testTermin2.setStp(sdf.parse("2011.02.15-10:45:00"));				//Endzeitpunkt des neuen Termin setzen
        testTermin2.setQuelle(1); // 1 = automatisch 2 = manuell hinzugefügt
        testTermin2.setPrio(9);
        testTermin2.setVbz(15);
        testTermin2.setNbz(15);
        testTermin2.setOrt("Mittweida");
        testTermin2.setTyp("Seminar");
        testTermin2.setBem("");
        testTerminsatz.tAnlegen(testTermin2);
        System.out.println("Termin 2 eingefügt");
/*
        testTermin3 = new Termin();
        testTermin3.setBez("C#");                           //neues Terminobjekt anlegen
        testTermin3.setStt(sdf.parse("2009.03.09-12:45:00"));				//Startzeitpunkt des neuen Termin setzen
        testTermin3.setStp(sdf.parse("2009.03.08-14:00:00"));				//Endzeitpunkt des neuen Termin setzen
        testTermin3.setQuelle(1); // 1 = automatisch 2 = manuell hinzugefügt
        testTermin3.setPrio(-1);
        testTermin3.setVbz(0);
        testTermin3.setNbz("ABC");
        testTermin3.setOrt("//////.");
        testTermin3.setTyp("Praktikum");
        testTermin3.setBem("1337");
        System.out.println("Termin einfügen: " + testTerminsatz.tAnlegen(testTermin3));

        testTermin4 = new Termin();
        testTermin4.setBez("Zahnarzt");                           //neues Terminobjekt anlegen
        testTermin4.setStt(sdf.parse(""));				//Startzeitpunkt des neuen Termin setzen
        testTermin4.setStp(sdf.parse("2013.AA.32-25:61:00"));				//Endzeitpunkt des neuen Termin setzen
        testTermin4.setQuelle(2); // 1 = automatisch 2 = manuell hinzugefügt
        testTermin4.setPrio("A");
        testTermin4.setVbz("<---"");
        testTermin4.setNbz("---");
        testTermin4.setOrt("Praxis");
        testTermin4.setTyp("Zahnarzt");
        testTermin4.setBem("----");
        System.out.println("Termin einfügen: " + testTerminsatz.tAnlegen(testTermin4));
*/    }

    /**
     * Ausgangsbedingungen nullen
     */
    @After
    public void tearDown() {
        testTerminsatz.tEntfernen(testTermin1);
        testTerminsatz.tEntfernen(testTermin2);
        System.out.println("genullt");
    }

    /**
     * Test, ob Terminsatzobjekt existiert
     */
    @Test
    public void testTerminsatz(){
        assertNotNull(testTerminsatz);
        System.out.println("Terminsatz NotNull");
    }

    /**
     * Test, ob Terminobjekte existieren
     */
    @Test
    public void testTermine(){
        //Test ob testTermin1 vorhanden
        assertNotNull(testTermin1);
        System.out.println("Termin 1 NotNull");
        assertNotNull(testTermin2);
        System.out.println("Termin 2 NotNull");
    }

    /**
     * Test. ob Termine korrekt in das System übernommen
     * und wieder ausgelesen werden können
     * @throws ParseException
     */
    @Test
    public void testTerminKorrektImSystem() throws ParseException{
        //Test ob korrekt ins System übernommen
        //Termin 1
        assertEquals("Vorlesung Softwaretechnik",testTermin1.getBez());
        assertEquals("2011.02.15-09:45:00",testTermin1.getSttStr());
        assertEquals("2011.02.15-11:15:00",testTermin1.getStpStr());
        assertEquals("8-203",testTermin1.getOrt());
        assertEquals(0, (int) testTermin1.getVbz());
        assertEquals(30, (int) testTermin1.getNbz());
        assertEquals("Schreibzeug mitnehmen",testTermin1.getBem());
        assertEquals("Vorlesung",testTermin1.getTyp());
        assertEquals(10, (int)testTermin1.getPrio());
        assertEquals(1, (int)testTermin1.getQuelle());
        System.out.println("Termin 1 Korrekt im System");
        //Termin 2
        assertEquals("Seminar",testTermin2.getBez());
        assertEquals("2011.02.15-09:15:00",testTermin2.getSttStr());
        assertEquals("2011.02.15-10:45:00",testTermin2.getStpStr());
        assertEquals("Mittweida",testTermin2.getOrt());
        assertEquals(15, (int) testTermin2.getVbz());
        assertEquals(15, (int) testTermin2.getNbz());
        assertEquals("",testTermin2.getBem());
        assertEquals("Seminar",testTermin2.getTyp());
        assertEquals(9, (int)testTermin2.getPrio());
        assertEquals(1, (int)testTermin2.getQuelle());
        System.out.println("Termin 2 Korrekt im System");
    }

    /**
     * Test, ob Datumsfehler erkannt werden
     * (Ende < Start)
     * @throws ParseException
     */
    @Test (expected = java.lang.IllegalArgumentException.class)
    public void testDatumKorrekt() throws ParseException{
        //letzten testTermin1 entfernen
        testTerminsatz.tEntfernen(testTermin1);
        //test ob ungültige Zeiten erkannt werden (Ende < Start)
        testTermin1.setStt(sdf.parse("2011.02.15-11:15:00"));
        testTermin1.setStp(sdf.parse("2011.02.15-09:45:00"));
        testTerminsatz.tAnlegen(testTermin1);
        System.out.println("Datum vertauscht wirft Exception");

    }
/*
    @Test
    public void testZeitabrechnung(){
        java.awt.event.ActionEvent evt;
        semesterplaner.gui.Gui.(evt);
    }*/
    /*
    //Konfliktlösung fehlt noch
    @Test
    public void testAutoPriorität() throws ParseException{
        //letzten testTermin1 entfernen
        testTerminsatz.tEntfernen(testTermin1);
        //Autokonfliktlösung testen
        testTermin1.setStt(sdf.parse("2011.02.15-09:45:00"));
        testTermin1.setStp(sdf.parse("2011.02.15-11:15:00"));
        System.out.println("Termin einfügen: " + testTerminsatz.tAnlegen(testTermin1));
        testTerminsatz.tAuslesen(TerminAttribut.bez);
    }*/

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

}