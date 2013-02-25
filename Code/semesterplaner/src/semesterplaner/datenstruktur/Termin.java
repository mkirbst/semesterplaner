package semesterplaner.datenstruktur;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**Klasse Termin
 * Beinhaltet Terminattribute sowie die benötigten Zugriffsfunktionen
 *
 * @author m
 */
public class Termin implements Serializable  {

    public static String datumFormat = "yyyy.MM.dd-HH:mm:ss";	//Jahr.Monat.Tag-Stunde:Minute:Sekunde
    SimpleDateFormat sdf = new SimpleDateFormat(datumFormat);

    //Klassenattribute
    protected String		bez		= "";           //Bezeichnung des Termins
    protected Date 		stt		= new Date(0); 	//Startzeitpunkt in [ms] seit 01.Jan 1970 00:00 GMT
    protected Date 		stp		= new Date(0); 	//Stopzeitpunkt in [ms] seit 01.Jan 1970 00:00 GMT
    protected String		ort		= "";		//Ort
    protected int                 vbz		= 0;		//Vorbereitungszeit in Minuten
    protected int                 nbz		= 0;		//Nachbereitungszeit in Minuten
    protected String		bem		= "";		//Bemerkungen
    protected String		typ		= "";		//Termintyp
    protected int 		prio            = 0;		//Priorität des Termins 0-5, 0=Initialwert
    protected int                 quelle          = 0;		//Quelle des Termins 0=Initialwert 1=auto 2=manuell

    //Zugriffsfunktionen
    //get
    public String 		getBez()    {return bez;            }
    public Date 		getStt()    {return stt;            }
    public Date 		getStp()    {return stp;            }
    public String 		getSttStr() {return sdf.format(stt);}
    public String 		getStpStr() {return sdf.format(stp);}
    public String 		getOrt()    {return ort;            }
    public Integer		getVbz()    {return vbz;            }
    public Integer		getNbz()    {return nbz;            }
    public String		getBem()    {return bem;            }
    public String		getTyp()    {return typ;            }
    public Integer		getPrio()   {return prio;           }
    public Integer		getQuelle() {return quelle;         }
    public String		getId()  {return sdf.format(stt)+";"+sdf.format(stp)+";"+bez+";"+ort+";"+vbz+";"+nbz+";"+bem+";"+typ+";"+prio+";"+quelle;}

    //set
    public void setBez(String tBez)		{bez = tBez.trim();	}
    public void setStt(Date tStt)		{stt = tStt;		}
    public void	setStp(Date tStp)		{stp = tStp;		}
    public void	setOrt(String tOrt)		{ort = tOrt.trim();	}
    public void setVbz(int tVbz)		{vbz = tVbz;		}
    public void	setNbz(int tNbz)		{nbz = tNbz;		}
    public void	setBem(String tBem)		{bem = tBem.trim();	}
    public void setTyp(String tTyp)		{typ = tTyp.trim();	}
    public void	setPrio(int tPrio)		{prio = tPrio;		}
    public void setQuelle(int tQuelle)          {quelle = tQuelle;	}

    public String toString() {
            return this.getId();
    }
}
