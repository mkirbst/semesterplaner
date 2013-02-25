package semesterplaner.importt.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.*;

/**
 * Ausfuhrende Klasse fuer den SSL-Killer. Erstellt mit der Hilfe 
 * von : http://www.howardism.org/Technical/Java/SelfSignedCerts.html
 * @author Christian Vorberg
 *
 */
public class Execute 
{
	private static SSLSocketFactory sslSocketFactory;

	/**
	 * Returns a SSL Factory instance that accepts all server certificates.
	 * <pre>SSLSocket sock =
	 *     (SSLSocket) getSocketFactory.createSocket ( host, 443 ); </pre>
	 * @return  An SSL-specific socket factory. 
	 **/
	public static final SSLSocketFactory getSocketFactory()
	{
	  if ( sslSocketFactory == null ) {
	    try {
	      TrustManager[] tm = new TrustManager[] { new NaiveTrustManager() };
	      SSLContext context = SSLContext.getInstance ("SSL");
	      context.init( new KeyManager[0], tm, new SecureRandom( ) );

	      sslSocketFactory = (SSLSocketFactory) context.getSocketFactory ();

	    } catch (KeyManagementException e) {
	      System.err.println("No SSL algorithm support: " + e.getMessage()); 
	      e.printStackTrace(System.err);
	    } catch (NoSuchAlgorithmException e) {
	      System.err.println("Exception when setting up the Naive key management.");
	      e.printStackTrace(System.err);
	    }
	  }
	  return sslSocketFactory;
	}
	
	/**
	 * Setzt den TrustManager der aktuellen Session neu.
	 */
	public static void execute()
	{
		System.setProperty ( "javax.net.ssl.trustStore ", 
	       "semesterplaner.importt.ssl.NaiveTrustManager");
	}
	
}
