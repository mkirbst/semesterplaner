/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.export;

import java.io.IOException;
import java.util.TreeSet;
import semesterplaner.exceptions.ContainerException;
import semesterplaner.datenstruktur.*;

/**
 * Eine Abstrakte Klasse zum Exportieren von Terminen.
 * @author Christian Vorberg
 */
public abstract class ExportClass
{
	/**
	 * Der Pfad zur zu-Importierenden Datei
	 */
	protected String filename = null;

        /**
	 * Erzeugt die Exportklasse
	 *
	 * @param path der Pfad wo die Exportierte Datei liegen soll
	 */
        public ExportClass(String path)
        {
            filename = path;
        }

        /**
         * Schreibt die Export-Datei
         *
         * @param treeset Das Treeset die, die Termine enthaelt, die die Exportierten Termine enthaelt
         *
         * @throws IOException Bei I/O Fehlern.
	 * @throws ContainerException Die Funktion sammelt alle Funktionen auf, die sie von ihren Unter-Funktionen bekommen hat, und wirft diese weiter.
         */
        public abstract void proceed(TreeSet<Termin> treeset) throws IOException,ContainerException;

}

