/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.export;

import java.io.*;
import java.util.TreeSet;
import semesterplaner.datenstruktur.Termin;
import semesterplaner.exceptions.ContainerException;

/**
 * Exportiert in einer Bin&auml;rdatei.
 *
 * @author Christian Vorberg
 */
public class BinaryEx extends ExportClass
{
    /**
     * Erzeugt den Binaer-Export
     * @param path Der Pfad wo die Datei gespeichert werden soll.
     */
    public BinaryEx(String path)
    {
        super(path);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException Wenn der Pfad oder der Treeset null ist.
     */
    @Override
    public void proceed(TreeSet<Termin> treeset) throws IOException, ContainerException
    {
        if (this.filename == null)
            throw new IllegalArgumentException("Dateipfad zur Importierenden Datei ist leer!");
        
        if (treeset == null)
            throw new IllegalArgumentException("Das Treeset darf nicht null sein!");
        
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.filename)));
        oos.writeObject(treeset);
        oos.close();
    }
}
