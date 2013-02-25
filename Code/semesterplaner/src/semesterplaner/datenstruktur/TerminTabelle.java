/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.datenstruktur;

/**
 *
 * @author christian
 */
public class TerminTabelle extends Termin {

    public TerminTabelle(Termin t)
    {
        bez = t.getBez();
        stt = t.getStt();
        stp = t.getStp();
        ort = t.getOrt();
        vbz = t.getVbz();
        nbz = t.getNbz();
        bem = t.bem;
        typ = t.typ;
        prio = t.prio;
        quelle = t.quelle;
    }

    public String toString() {
            return this.getBez()+" -- "+this.getOrt();
    }

}
