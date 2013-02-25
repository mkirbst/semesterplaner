package semesterplaner.libs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Loesung fuer das Zertifikat-Ignorieren fuer HTWM von:
 * http://stackoverflow.com/questions/3670503/java-ssl-tls-ignore-expired-cert-java-security-cert-certpathvalidatorexception
 * 
 * @author Umsetzung: Christian Vorberg, Vorgehensweise von obiger URL
 */
public class Downloader 
{
        private static java.net.URL burl;

	/**
	 * Liefert einen InputStream fuer die angegebene URL.
	 * Nur http und https erlaubt.
	 * @param url Die URL, fuer die ein InputStream gebraucht wird.
	 * @return Ein InputStream zum Ziel. Wenn ein Fehler aufgetreten ist (z.B. 404) wird NULL zurueckgegeben.
	 * @throws IOException Bei I/O Fehler
	 * @throws IllegalArgumentException Wenn das Protokoll weder http noch https ist.
	 * @TODO Zuruecksetzen der SSL Modifikationen nach dem Download.
	 */
	public static InputStream download(java.net.URL url) throws IOException
	{
		URLConnection connect;
		HttpURLConnection httpconn;
		
                burl = url;
                
		if (url.getProtocol().toLowerCase().equals("http"))
		{
			//Nothing, lol
		} else if (url.getProtocol().toLowerCase().equals("https"))
		{
			//Naiven Manager setzen
                        semesterplaner.importt.ssl.Execute.execute();
		}    
		else 
			throw new IllegalArgumentException("Das Protokoll: " + url.getProtocol() + " ist nicht erlaubt.");
		
		//Connection oeffnen
		connect = url.openConnection();
		httpconn = (HttpURLConnection) connect;

                /**
                 * @TODO: Exception abfangen bei keinem internet
                 */
		//Ueberpruefen ob das Dokument verfuegbar ist
		if (httpconn.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			System.err.println("DEBUG: ResponseCode: "  + httpconn.getResponseCode());
			return null;
		}
		
		//InputStream Zurueckgeben.
                return url.openStream();
	}

        /**
         * Hohlt die geladene URL als String
         * @return [NOURL] Wenn noch nichts geladen wurde.
         */
        public static String getStringURL()
        {
            if (burl != null)
            {
                return burl.toString();
            }
            else
            {
                return "[NOURL]";
            }

        }
}

