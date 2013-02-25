/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.importt;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import semesterplaner.exceptions.ContainerException;
import semesterplaner.datenstruktur.*;

/**
 * Importiert Termine aus einer Binaerdatei.
 * @author christian
 */
public class BinaryIm extends ImportClass {

    /**
     * {@inheritDoc}
     */
    public BinaryIm(Terminsatz ts)
    {
        super(ts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void proceed(Date imp_beginn, Date imp_ende) throws IOException, ContainerException
    {
        if (this.filename == null)
            throw new IllegalArgumentException("Dateipfad zur Importierenden Datei ist leer!");

        TreeSet<Termin> tree;
        Iterator I;
        Termin t1 = new Termin();
        Termin t2 = new Termin();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.filename)));

        if (imp_beginn != null)
            t1.setStt(imp_beginn);
        if (imp_ende != null)
            t2.setStt(imp_ende);
        else
            t2.setStt(new Date(Long.MAX_VALUE));
        
        try {
            tree = (TreeSet<Termin>) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException ex) {
            throw new ContainerException(ex);
        }
        
        tree = (TreeSet<Termin>) tree.subSet(t1, true, t2, true); //Termine holen, die interessant sind
        I = tree.iterator();
        while (I.hasNext())
        {
            t1 = (Termin) I.next();
             //Termin in Terminsatz einfuegen
            try
            {
                ts.tAnlegen(t1);
            } catch (IllegalArgumentException e)
            {
                if (!e.getMessage().equals("Element schon vorhanden !!"))
                    throw e;
                // ?? else if (Config.DEBUG) ??
            }
        }
    }
}
