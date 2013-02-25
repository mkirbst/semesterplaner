/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.libs;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import semesterplaner.datenstruktur.Termin;
import semesterplaner.datenstruktur.TerminComparator;
import semesterplaner.datenstruktur.TerminComparator.TerminAttribut;
import semesterplaner.datenstruktur.Terminsatz;

/**
 * Enthaelt alle Termine eines Tages und deren Konflikte
 * @author christian
 */
public class TerminKonfliktContainer
{
     ArrayList<TreeSet<Termin>> termin_konflikte = new ArrayList<TreeSet<Termin>>();

     int pre_cached_size = -1;

     /**
      * Baut die Termine mit den entsprechenden Konflikten in einen TreeSet auf.
      *
      * @param tree Termine die man untersuchen will
      * @param konfliktTermine Terminsatz der die Konflikte enthält
      */
     public void buildTree(final TreeSet<Termin> tree,final Terminsatz konfliktTermine)
     {
        int I_konf = 0;
        Iterator itr;
        Termin t_konf;
        TreeSet<Termin> addtree;
        TreeSet<Termin> tmptree;
        itr = tree.iterator();
        pre_cached_size = -1;
        while(itr.hasNext())
        {
            addtree = new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));

            t_konf = (Termin) itr.next();
            try {
                if (t_konf.getBez().indexOf("Spanisch") > -1)
                {

                //Termine holen die mit diesen in Konflikt stehen
                tmptree = konfliktTermine.tBeginntZwischenNonStrict(FunctionCollection.convertDatetoString(t_konf.getStt()),
                        FunctionCollection.convertDatetoString(t_konf.getStp()));

                System.out.println(FunctionCollection.convertDatetoString(t_konf.getStt()) + " " +
                        FunctionCollection.convertDatetoString(t_konf.getStp()) + "AA: " + tmptree.size());

                addtree.addAll(tmptree);
                tmptree = konfliktTermine.tEndetZwischenNonStrict(FunctionCollection.convertDatetoString(t_konf.getStt()),
                        FunctionCollection.convertDatetoString(t_konf.getStp()));
                addtree.addAll(tmptree);
                }
            } catch (ParseException ex) {
                Logger.getLogger(TerminKonfliktContainer.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (addtree.isEmpty())
                addtree.add(t_konf);
            termin_konflikte.add(addtree);
        }
     }

     /**
      * Hohlt die naechsten Termingruppe die Konflikte enhaelt.
      * Wird nach dem holen aus den Speicher der Klasse geloescht.
      * @return
      */
     public TreeSet<Termin> getKonfTermineNext()
     {
        Iterator<TreeSet<Termin>> I = termin_konflikte.iterator();
        TreeSet<Termin> tmp;
        if (I.hasNext())
        {
            tmp = I.next();
            termin_konflikte.remove(tmp);
            return tmp;
        }

        return null;
     }

     /**
      * Hohlt die erste Gruppe von Konflikt-Termine die den Termin t enthaelt
      * @param t
      * @return
      */
     public TreeSet<Termin> getKonfTermine(Termin t)
     {
        Iterator<TreeSet<Termin>> I = termin_konflikte.iterator();
        TreeSet<Termin> tmpset;
        while (I.hasNext())
        {
            tmpset = I.next();
            if (tmpset.contains(t))
            {
                return tmpset;
            }
        }

        return new TreeSet<Termin>(new TerminComparator(TerminAttribut.stt));
     }

     /**
      * Ein bearbeiter Termin kann geloescht werden, damit er nicht 2x auftaucht!
      * @param t
      */
     public void deleteTermin(Termin t)
     {
        ArrayList<TreeSet<Termin>> to_delete = new ArrayList<TreeSet<Termin>>();
        Iterator<TreeSet<Termin>> I = termin_konflikte.iterator();
        TreeSet<Termin> tmpset;
        while (I.hasNext())
        {
            tmpset = I.next();
            if (tmpset.contains(t))
                tmpset.remove(t);

            if (tmpset.size() == 0)
                to_delete.add(tmpset); //extra um den Iterator nicht zu verwirren
        }

        I = to_delete.iterator();
        while (I.hasNext())
            termin_konflikte.remove(I.next());
     }

     /**
      * Die maximale Anzahl von Konflikt-Terminen innerhalb dieser Gesamten-Konflikt-Gruppe. (erstellt mit buildtree())
      * Wird benutzt um die maximale Anzahl der Spalten herauszufinden, die Pro Tag angelegt werden muessen. (Anzahl der Konflikte in "einer Reihe")
      * @return
      */
     public int getMaxSize()
     {
        if (this.pre_cached_size > -1)
            return pre_cached_size;

        int size = 1;
        Iterator<TreeSet<Termin>> I = termin_konflikte.iterator();
        TreeSet<Termin> tmpset;
        while (I.hasNext())
        {
            tmpset = I.next();
            if (tmpset.size() > size)
                size = tmpset.size();
        }

        pre_cached_size = size;
        return size;
     }
}
