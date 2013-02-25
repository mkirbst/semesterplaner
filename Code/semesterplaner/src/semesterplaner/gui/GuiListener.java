package semesterplaner.gui;

import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.fop.apps.FOPException;
import semesterplaner.datenstruktur.TerminComparator.TerminAttribut;
import semesterplaner.datenstruktur.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.Integer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import net.fortuna.ical4j.data.ParserException;
import semesterplaner.exceptions.ContainerException;
import semesterplaner.export.BinaryEx;
import semesterplaner.export.ExportClass;
import semesterplaner.gui.Gui;
import semesterplaner.importt.BinaryIm;
import semesterplaner.importt.HTWMStundenplanImport;
import semesterplaner.importt.ICalender;
import semesterplaner.importt.ImportClass;
import semesterplaner.libs.Downloader;
import semesterplaner.libs.FunctionCollection;
import semesterplaner.export.PDF;

public class GuiListener extends Gui{
    Boolean bDebug = true;
    String datumFormat = "yyyy.MM.dd-HH:mm:ss";	//Jahr.Monat.Tag-Stunde:Minute:Sekunde
    SimpleDateFormat sdf = new SimpleDateFormat(datumFormat);
    String jahr = "", mon = "", tag = "";
    String aDatum [] = {"", "", ""};  // jahr monat tag

    public GuiListener this_instance = this; //Wird benötigt für den Import beim FileChooser

    /**Konstruktor GuiListener
     * @param ts Terminsatz für die interne Datenhaltung
     * @param tsz Terminsatz für die Zeitraumübersicht ( ts != tsz wenn Suche in der Zrü dargestellt werden soll)
     * @param config enthält KOnfiguration
     */
    public GuiListener(Terminsatz ts, Terminsatz tsz, Config config) {
        super(ts, config);
        gtsz = tsz;

        jbDmOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                        jbDmOkActionPerformed(ae);

            }

            private void jbDmOkActionPerformed(ActionEvent ae) {
                Termin tDm = new Termin();
                String sStd = "1970.01.01-00:00:00";    //standartwert für Start- und Endzeitpunkt -> ignorieren bei der Suche
                String sStt = jcbDmSttJahr.getSelectedItem()+"."+jcbDmSttMon.getSelectedItem()+"."+jcbDmSttTag.getSelectedItem()+"-"+jcbDmSttStd.getSelectedItem()+":"+jcbDmSttMin.getSelectedItem()+":"+jcbDmSttSek.getSelectedItem();
                String sStp = jcbDmStpJahr.getSelectedItem()+"."+jcbDmStpMon.getSelectedItem()+"."+jcbDmStpTag.getSelectedItem()+"-"+jcbDmStpStd.getSelectedItem()+":"+jcbDmStpMin.getSelectedItem()+":"+jcbDmStpSek.getSelectedItem();
                String sVbz = jftfDmVbz.getText();
                String sNbz = jftfDmNbz.getText();


                tDm.setBez(jtfDmBez.getText());
                try
                {
                    if(!(sStt.equals(sStd))) tDm.setStt(sdf.parse(sStt));
                    if(!(sStp.equals(sStd))) tDm.setStp(sdf.parse(sStp));
                } catch (ParseException e)
                {
                    this_instance.showErrorMessage("Fehler: " + e.getLocalizedMessage());
                    return;
                }

                tDm.setOrt(jtfDmOrt.getText());
                if(sVbz.equals("")) tDm.setVbz(0);
                    else            tDm.setVbz(Integer.parseInt(sVbz));
                if(sNbz.equals("")) tDm.setNbz(0);
                    else            tDm.setNbz(Integer.parseInt(sNbz));
                tDm.setTyp(jtfDmTyp.getText());
                if(jcbDmPrio.getSelectedIndex() == 0)   tDm.setPrio(3); //3=Defaultwert
                else                                    tDm.setPrio(Integer.parseInt((String)jcbDmPrio.getSelectedItem())); //very ugly
                tDm.setBem(jtaDmBem.getText());
                tDm.setQuelle(2);   //Quelle: 0=init 1=auto 2=manuell hinzugefügt




                if(jrbDmSuchen.isSelected())  {
                    gtsz.clear();
                    if (this_instance.start_date_ch.isSelected())
                        tDm.setStt(new Date(0));
                    if (this_instance.ende_date_ch.isSelected())
                        tDm.setStp(new Date(0));
                    
                    gtsz.tsAnlegen(gts.tSuche(tDm));
                    System.out.println("Suchvorgang abgeschlossen: " + gtsz.size() + " Treffer aus " + gts.size() + (" Terminen."));
                    if(gtsz.size()>0)
                    {
                        this_instance.ges_tables_repaint();
                        jTabbedPane1.setSelectedIndex(1);   //springe zur Zeitraumübersicht
                    }
                        jTabbedPane1.setSelectedIndex(1);   //springe zur Zeitraumübersicht
                    
                    if(Config.DEBUG) {
                        System.out.println("rb suchen gedrückt");
                        
                    }
               }
                else if (jrbDmAnlegen.isSelected())  {
                    //Musskriterien: Bez, Stt,Stp!=1970..., Quelle, Prio

                    if(tDm.getBez().isEmpty() || tDm.getSttStr().equals(sStd) || tDm.getStpStr().equals(sStd))  {
                        System.out.println("FEHLER BEIM ANLEGEN DES TERMINS: Bezeichner , Start-, und Endzeitpunkt müssen angegeben werden !!");
                    } else if (tDm.getStt().after(tDm.getStp())){
                        System.out.println("FEHLER BEIM ANLEGEN DES TERMINS: Startzeitpunkt muss vor Endzeitpunkt liegen !!");

                    } else {
                        System.out.println("Neuer Termin wird manuell angelegt:  "+ tDm);
                        try
                        {
                            gts.tAnlegen(tDm);
                        } catch (IllegalArgumentException e)
                        {
                            this_instance.showErrorMessage("Termin bereits vorhanden!");
                        }
                    }
                } else if(jrbDmLoeschen.isSelected()) {
                    if(gts.tEntfernen(tDm).isEmpty()) this_instance.showMessage("Termin gelöscht: "+tDm.getBez());
                    else this_instance.showErrorMessage("Termin konnte nicht gelöscht werden:\n"+tDm.getBez());
                }
                if(jrbDmExportieren.isSelected()) {
                   JFileChooser chooser = new JFileChooser();
                   chooser.setFileFilter(new FileFilter() {
                            public boolean accept(File f) {
                                return f.getName().toLowerCase().endsWith(".pdf") || f.isDirectory();
                            }
                            public String getDescription() {
                                return "PDF (*.pdf)";
                            }

                   });
                   int returnVal = chooser.showSaveDialog(this_instance);
                   if(returnVal == JFileChooser.APPROVE_OPTION)
                   {
                      String filename = chooser.getSelectedFile().getPath();
                      if (!filename.endsWith(".pdf")) //nicht mit eingegeben
                        filename += ".pdf";

                      exportSomething(new semesterplaner.export.PDF(filename, gts),false);
                   }
                }


               
            }
        });

        //Mauslistener für die einzelnen Monate der Zeitraumübersicht
        jtJan.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtJanActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
            
        });

        jtFeb.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtFebActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtMae.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtMaeActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtApr.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtAprActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtMai.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtMaiActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtJun.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtJunActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtJul.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtJulActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtAug.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtAugActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtSep.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtSepActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtOkt.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtOktActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtNov.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtNovActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        jtDez.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(1);
                try {
                    jtDezActionPerformed(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("mouse released");
            }
            public void mouseClicked(MouseEvent e)  {}
            public void mousePressed(MouseEvent e)  {}
            public void mouseEntered(MouseEvent e)  {}
            public void mouseExited(MouseEvent e)   {}
        });

        /// Termine Import Action ///
        termineImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                //FileCooser
                   JFileChooser chooser = new JFileChooser();
                   chooser.setFileFilter(new FileFilter() {
                            public boolean accept(File f) {
                                return f.getName().toLowerCase().endsWith(".ical") || f.getName().toLowerCase().endsWith(".ics") || f.getName().toLowerCase().endsWith(".saf") || f.isDirectory();
                            }
                            public String getDescription() {
                                return "iCalender oder Semesterplaner-Binär-Dateien (*.ical, *.ics, *.saf)";
                            }
                   });
                   int returnVal = chooser.showOpenDialog(this_instance);
                   if(returnVal == JFileChooser.APPROVE_OPTION) 
                   {
                      if (chooser.getSelectedFile().getName().endsWith(".ical") || chooser.getSelectedFile().getName().endsWith(".ics")) //ical format
                      {
                          importSomething(chooser.getSelectedFile(),new semesterplaner.importt.ICalender(gts));
                      }
                      else //binaerformat
                      {
                          importSomething(chooser.getSelectedFile(),new BinaryIm(gts));
                      }
                      this_instance.ges_tables_repaint();
                      repaint_7_tages_uebersicht();
                   }
            }
        }); //ActionPerformed

        termine_htwm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doHtwmImport(true);
                this_instance.ges_tables_repaint();
                repaint_7_tages_uebersicht();
            }

        });

        //Terminsatz auf die Konsole ausgeben (DEBUG) //
        if (Config.DEBUG)
        this.tsatz_ausgeben.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                gts.tAuslesen(TerminAttribut.stt);
            }

        });

        // iCalender Termine Exportieren //
        termiCalExp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                   chooser.setFileFilter(new FileFilter() {
                            public boolean accept(File f) {
                                return f.getName().toLowerCase().endsWith(".ics") || f.isDirectory();
                            }
                            public String getDescription() {
                                return "iCalender (*.ics)";
                            }
                  
                   });
                   int returnVal = chooser.showSaveDialog(this_instance);
                   if(returnVal == JFileChooser.APPROVE_OPTION)
                   {
                      String filename = chooser.getSelectedFile().getPath();
                      if (!filename.endsWith(".ics")) //nicht mit eingegeben
                        filename += ".ics";
                      
                      exportSomething(new semesterplaner.export.ICalender(filename),false);
                      repaint_7_tages_uebersicht();
                   }
            }
        });

        // Binaer Termine Exportieren //
        termineArchi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                   chooser.setFileFilter(new FileFilter() {
                            public boolean accept(File f) {
                                return f.getName().toLowerCase().endsWith(".saf") || f.isDirectory();
                            }
                            public String getDescription() {
                                return "iCalender (*.saf)";
                            }
                   });
                   int returnVal = chooser.showSaveDialog(this_instance);
                   if(returnVal == JFileChooser.APPROVE_OPTION)
                   {
                      String filename = chooser.getSelectedFile().getPath();
                      if (!filename.endsWith(".saf")) //nicht mit eingegeben
                        filename += ".saf";

                      exportSomething(new BinaryEx(filename),true);
                      ges_tables_repaint();
                      repaint_7_tages_uebersicht();
                   }
            }
        });

        // PrevWeek Control
        prevWeek.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                        System.out.println("prevWeek");

                //Nextweek nehmen und um 2 wochen verringern
                Calendar c = (Calendar) nextweek.clone();
                c.add(Calendar.DAY_OF_MONTH, -14);
               setWeek(c);
            }
        });

        // NextWeek Control
        nextWeek.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("nextWeek");

                Calendar c = (Calendar) nextweek.clone();

                setWeek(c); //auf nächste Woche gehen
            }
        });


    }

    //MouseListener für Zeitraumübersicht
    private void jtJanActionPerformed(MouseEvent e) throws ParseException {
        String[] aDatum = jtJan.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtJan.getValueAt(jtJan.getSelectedRow(), jtJan.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr + "." + mon + "." + tag + "-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);


        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);


        jTabbedPane1.setSelectedIndex(0);
        if (bDebug) {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtJan.getSelectedColumn());
            System.out.println("Row: " + jtJan.getSelectedRow());
            System.out.println("Tag: " + jtJan.getValueAt(jtJan.getSelectedRow(), jtJan.getSelectedColumn()).toString() + "." + jtJan.getName());
            System.out.println("generiertes Datum gemäß sdf: " + sdf.format(dTagGeklickt));
        }
    }
    private void jtFebActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtFeb.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtFeb.getValueAt(jtFeb.getSelectedRow(), jtFeb.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtFeb.getSelectedColumn());
            System.out.println("Row: " + jtFeb.getSelectedRow());
            System.out.println("Tag: " + jtFeb.getValueAt(jtFeb.getSelectedRow(), jtFeb.getSelectedColumn()).toString()+ "."+jtFeb.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtMaeActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtMae.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtMae.getValueAt(jtMae.getSelectedRow(), jtMae.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtMae.getSelectedColumn());
            System.out.println("Row: " + jtMae.getSelectedRow());
            System.out.println("Tag: " + jtMae.getValueAt(jtMae.getSelectedRow(), jtMae.getSelectedColumn()).toString()+ "."+jtMae.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtAprActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtApr.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtApr.getValueAt(jtApr.getSelectedRow(), jtApr.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtApr.getSelectedColumn());
            System.out.println("Row: " + jtApr.getSelectedRow());
            System.out.println("Tag: " + jtApr.getValueAt(jtApr.getSelectedRow(), jtApr.getSelectedColumn()).toString()+ "."+jtApr.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtMaiActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtMai.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtMai.getValueAt(jtMai.getSelectedRow(), jtMai.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtMai.getSelectedColumn());
            System.out.println("Row: " + jtMai.getSelectedRow());
            System.out.println("Tag: " + jtMai.getValueAt(jtMai.getSelectedRow(), jtMai.getSelectedColumn()).toString()+ "."+jtMai.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtJunActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtJun.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtJun.getValueAt(jtJun.getSelectedRow(), jtJun.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtJun.getSelectedColumn());
            System.out.println("Row: " + jtJun.getSelectedRow());
            System.out.println("Tag: " + jtJun.getValueAt(jtJun.getSelectedRow(), jtJun.getSelectedColumn()).toString()+ "."+jtJun.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtJulActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtJul.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtJul.getValueAt(jtJul.getSelectedRow(), jtJul.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtJul.getSelectedColumn());
            System.out.println("Row: " + jtJul.getSelectedRow());
            System.out.println("Tag: " + jtJul.getValueAt(jtJul.getSelectedRow(), jtJul.getSelectedColumn()).toString()+ "."+jtJul.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtAugActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtAug.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtAug.getValueAt(jtAug.getSelectedRow(), jtAug.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtAug.getSelectedColumn());
            System.out.println("Row: " + jtAug.getSelectedRow());
            System.out.println("Tag: " + jtAug.getValueAt(jtAug.getSelectedRow(), jtAug.getSelectedColumn()).toString()+ "."+jtAug.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtSepActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtSep.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtSep.getValueAt(jtSep.getSelectedRow(), jtSep.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtSep.getSelectedColumn());
            System.out.println("Row: " + jtSep.getSelectedRow());
            System.out.println("Tag: " + jtSep.getValueAt(jtSep.getSelectedRow(), jtSep.getSelectedColumn()).toString()+ "."+jtSep.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtOktActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtOkt.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtOkt.getValueAt(jtOkt.getSelectedRow(), jtOkt.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtOkt.getSelectedColumn());
            System.out.println("Row: " + jtOkt.getSelectedRow());
            System.out.println("Tag: " + jtOkt.getValueAt(jtOkt.getSelectedRow(), jtOkt.getSelectedColumn()).toString()+ "."+jtOkt.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtNovActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtNov.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtNov.getValueAt(jtNov.getSelectedRow(), jtNov.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtNov.getSelectedColumn());
            System.out.println("Row: " + jtNov.getSelectedRow());
            System.out.println("Tag: " + jtNov.getValueAt(jtNov.getSelectedRow(), jtNov.getSelectedColumn()).toString()+ "."+jtNov.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }
    private void jtDezActionPerformed(MouseEvent e) throws ParseException  {
        String [] aDatum = jtDez.getName().split("\\.");
        jahr = aDatum[1];
        mon = aDatum[0];
        tag = jtDez.getValueAt(jtDez.getSelectedRow(), jtDez.getSelectedColumn()).toString();
        bTagGeklickt = true;
        sTagGeklickt = jahr +"."+ mon +"."+ tag +"-00:00:00";
        dTagGeklickt = sdf.parse(sTagGeklickt);

        Calendar c = new GregorianCalendar();
        c.set(Integer.valueOf(jahr), Integer.valueOf(mon)-1, Integer.valueOf(tag));
        setWeek(c);

        jTabbedPane1.setSelectedIndex(0);
        if(bDebug)  {
            System.out.println("DEBUG: Zeitraumübersicht -> 7-Tagesübersicht.");
            System.out.println("Col: " + jtDez.getSelectedColumn());
            System.out.println("Row: " + jtDez.getSelectedRow());
            System.out.println("Tag: " + jtDez.getValueAt(jtDez.getSelectedRow(), jtDez.getSelectedColumn()).toString()+ "."+jtDez.getName());
            System.out.println("generiertes Datum gemäß sdf: "+sdf.format(dTagGeklickt));
        }
    }

    private void importSomething(File f, ImportClass im)
    {
        im.setFilepath(f.getPath());
        try {
            im.proceed(null, null);
        } catch (IOException ex) {
            showErrorMessage("I/O Fehler aufgetreten:" + ex.getLocalizedMessage());
            return;
        } catch (ContainerException ex) {
            if (ex.getException() instanceof ClassNotFoundException) //Special Binaer
            {
                showErrorMessage("Konnte die Import-Datei Datei \"" + f.getPath() + "\" nicht auslesen! (Keine Gueltige Importdatei fuer Semesterplaner?)");
                return;
            }
            else
                showErrorMessage("Unbekannter Fehler: " + ex.getException().getLocalizedMessage());
            return;
        }

        config.setDirty(true);
        showMessage("Import Erfolgreich.");
    }

    /**
     * Exportiert mit hilfe der angegebenen Klasse alle Termine.
     * @param iex Die abgeleitete Klasse (Typ) von der Exportiert werden soll
     * @param archivierung Wenn true, werden die Exportierten Termine geloescht. (nur bis zum Heutigen Tag wird in diesen Fall exportiert / geloescht)
     */
    private void exportSomething(ExportClass iex, boolean archivierung)
    {
        TreeSet<Termin> termine;
        try {
            
            if (!archivierung)
                termine = gts.tBeginntZwischen(FunctionCollection.convertDatetoString(new Date(0)), FunctionCollection.convertDatetoString(new Date(Long.MAX_VALUE)));
            else
                //nur bis jetzt archivieren!
                termine = gts.tBeginntZwischen(FunctionCollection.convertDatetoString(new Date(0)), FunctionCollection.convertDatetoString(new Date(System.currentTimeMillis())));

            iex.proceed(termine);
        } catch (IOException ex) {
            showErrorMessage("I/O Fehler: " + ex.getLocalizedMessage());
            return;
        } catch (ParseException ex) {
            showErrorMessage("(unmoeglicher Fehler) Parsen des Datums fehlgeschlagen!");
            return;
        } catch (ContainerException ex) {
            showErrorMessage("Unbekannter Fehler: " + ex.getException().getLocalizedMessage());
            return;
        }
        if (archivierung)
        {
            //Archivierte Termine loeschen
            Iterator I = termine.iterator();
            while (I.hasNext())
            {
                gts.tEntfernen((Termin) I.next());
            }
            config.setDirty(true);
            showMessage("Archivierung Erfolgreich.");
        }
        else
        {
            showMessage("Export Erfolgreich.");
            
        }
    }

    /**
     * Fuehrt den HTWM-Import durch
     * @param erfolg_melden Ob ein Erfolg mittels Messagebox ausgegeben werden soll.
     */
    private void doHtwmImport(boolean erfolg_melden)
    {
        HTWMStundenplanImport im = new HTWMStundenplanImport(gts,HTWMStundenplanImport.IM_ICAL);
        String filepath = config.getTempPfad() + config.getSemGr() + HTWMStundenplanImport.FILE_TEMP_POSTFIX;
        im.setSeminargruppe(config.getSemGr());
        im.setFilepath(filepath);
        boolean error = false;
        if (!im.downloadSeminargruppe())
        {
            showErrorMessage("Konnte keine iCalender Datei der Seminargruppe: " + config.getSemGr() + " downloaden. URL: " + Downloader.getStringURL());
            im.removeSeminargruppeVerweis();
            return;
        }
        
        Date heute = new Date(System.currentTimeMillis());
        try {
            im.proceed(heute, null);
        } catch (IOException ex) {
            showErrorMessage("I/O Fehler aufgetreten:" + ex.getLocalizedMessage());
        } catch (ContainerException ex) {
            if (ex.getException() instanceof ParserException)
            {
                showErrorMessage("Konnte die Heruntergeladene iCal Datei nicht auslesen! (" + Downloader.getStringURL() + ")(Keine iCal Datei, Serverfehler?)");
                error = true;
            }
            else
            {
                showErrorMessage("Unbekannter Fehler: " + ex.getLocalizedMessage());
                error = true;
            }
        } finally
        {
            if (!im.removeSeminargruppeVerweis())
                showErrorMessage("Konnte die Verweis auf die Datei nicht loeschen. " + im.getFilepath());
        }

        if (erfolg_melden && !error)
        {
            config.setDirty(true);
            showMessage("Import  von HTWM Erfolgreich.");
        }
    }

   

    public void setGuiElements(TreeSet<Termin> tNeueWerte) {

    }

    public TreeSet<Termin> getGuiElements() {
        TreeSet<Termin> tErg = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stp));
        return tErg;
    }


}
