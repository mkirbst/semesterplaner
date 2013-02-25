/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.gui;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import semesterplaner.datenstruktur.Termin;
import semesterplaner.datenstruktur.Terminsatz;

/** CustomCellRenderer
 *  Erlaubt im Gegensatz zum DefaultCellRenderer das einfärben einzelner Zellen
 * @author m
 */
public class CustomCellRenderer extends DefaultTableCellRenderer {
    Terminsatz rts;
    Terminsatz rtsk;

    /**Konstruktor CustomCellRenderer
     * Übernimmt Terminsätze nach denen die Zeitraumübersicht eingefärbt werden soll
     * @param ts Terminsatz der die Termine enthält die grün gefärbt werden
     * @param tsk Terminsatz der die Konflikttermine enthält, die orange gefärbt werden
     */
    public CustomCellRenderer(Terminsatz ts, Terminsatz tsk) {
        rts = ts;
        rtsk = tsk;
    }
    @Override   //überschreibe default getTableCellRenderer
    public Component getTableCellRendererComponent(JTable table, Object v, boolean sel, boolean focus, int x, int y) {
  
        Component cell = super.getTableCellRendererComponent(table, v, sel, focus, x, y);
        JTable jtemp = (JTable) table;
        if(v instanceof Integer) {  //Nur ausgefüllte Zellen
            try {
                //Nur ausgefüllte Zellen
                Integer i = (Integer) v;
                String sZellDatum = i + "." + table.getName();
                /*DEBUG*/
                //System.out.println("DEBUG-ccr: str: " + sZellDatum);
                StringTokenizer str = new StringTokenizer(sZellDatum, ".");
                String sZellTag = str.nextToken();
                if (sZellTag.length() == 1) {
                    sZellTag = "0" + sZellTag;
                }
                String sZellMon = str.nextToken();
                if (sZellMon.length() == 1) {
                    sZellMon = "0" + sZellMon;
                }
                String sZellJahr = str.nextToken();
                if (sZellJahr.length() == 1) {
                    sZellJahr = "0" + sZellJahr;
                }
                /*DEBUG*/
                //System.out.println("DEBUG-ccr: strtok: " + sZellTag + sZellMon + sZellJahr);
                String datumFormat = "yyyy.MM.dd-HH:mm:ss"; //Jahr.Monat.Tag-Stunde:Minute:Sekunde
                String renderDatumFormat = "d.M.yyyy"; //Jahr.Monat.Tag-Stunde:Minute:Sekunde
                SimpleDateFormat sdf = new SimpleDateFormat(renderDatumFormat);
                Date dHeute = new Date();
                String sHeute = sdf.format(dHeute);
                //rts.
                if (sZellDatum.equals(sHeute)) {
                    cell.setBackground(Color.BLUE);
                } else if (!(rtsk.tBeginntZwischen(sZellJahr+"."+sZellMon+"."+sZellTag+"-00:00:00",sZellJahr+"."+sZellMon+"."+sZellTag+"-23:59:59").isEmpty())) {
                    cell.setBackground(Color.ORANGE);
                } else if (!(rts.tBeginntZwischen(sZellJahr+"."+sZellMon+"."+sZellTag+"-00:00:00",sZellJahr+"."+sZellMon+"."+sZellTag+"-23:59:59").isEmpty())) {
                    cell.setBackground(Color.GREEN);
                } else {
                    cell.setBackground(Color.WHITE);
                }
            } catch (ParseException ex) {
                Logger.getLogger(CustomCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            cell.setBackground(Color.LIGHT_GRAY);
        }
         return cell;
    }
}
