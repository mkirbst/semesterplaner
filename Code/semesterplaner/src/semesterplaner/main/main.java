/**Programmausgabe zur Laufzeit siehe ganz unten**/
/**TODO:
 * - Warnungen beseitigen
 * - Konfliktlösung implementieren
 * - ...
 */

package semesterplaner.main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import semesterplaner.datenstruktur.TerminComparator.TerminAttribut;
import semesterplaner.datenstruktur.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JFrame;
import semesterplaner.exceptions.ContainerException;
import semesterplaner.gui.*;
import semesterplaner.importt.BinaryIm;
import semesterplaner.libs.RemoveEmptyLinesInputReader;

//DEBUG
@SuppressWarnings("unchecked")  
public class main {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException, IOException, Exception {
            String datumFormat = "yyyy.MM.dd-HH:mm:ss";	//Jahr.Monat.Tag-Stunde:Minute:Sekunde
            SimpleDateFormat sdf = new SimpleDateFormat(datumFormat);

            Terminsatz ts = new Terminsatz();
            Terminsatz tsz = new Terminsatz();

            Config config = new Config();

            GuiListener gl = new GuiListener(ts,tsz, config);

            gl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gl.setVisible(true);


            /*** vorhandene Termine einlesen ***/

            BinaryIm binim = new BinaryIm(ts);
            //Terminsatz tsatz = new Terminsatz();
            //BinaryIm binim = new BinaryIm(new Terminsatz());

            if (!config.ladeConfig(false))
                gl.showMessage("Config nicht gefunden. Standardwerte geladen.");
            

            /*
            //Nachschauen ob es eine solche datei gibt, ansonsten Ignorieren.
            File f = new File(config.getSpeicherPfad());
            try
            {
                if (f.exists())
                {
                    binim.setFilepath(config.getSpeicherPfad() + File.separatorChar + Config.SaveFile);
                    try {
                        binim.proceed(null, null);
                    } catch (ContainerException ex) {
                        throw ex.getException();
                    }
                }
            } catch (java.io.FileNotFoundException e)
            {
                //nichts zu tun
            }

*/
            gl.repaint_7_tages_uebersicht();
            gl.konflikteAktualisieren(true);
            gl.ges_tables_repaint();
            ts.tAuslesen(TerminAttribut.stt);
            
            
        }
}