package semesterplaner.exceptions;

/**
 * Eine ContainerException enthaelt weitere Exceptions, die weitergeworfen werden koennen.
 * @author Christian Vorberg
 */
public class ContainerException extends Exception 
{
	Exception got_exc = null;
	
	/**
	 * Erzeugt den Container und speichert die Exception.
	 *
	 * @param e Exception die nicht null ist
	 */
	public ContainerException(Exception e)
	{
		super("Das ist eine ContainerException. Sie enthaelt Exceptions. Deshalb diese Auswerten! Exception enthalten: " + e.getClass().toString());

		got_exc = e;
	}
	
	/**
	 * Holt die gespeicherte Exception
	 * @return Irgendeine Exception
	 */
	public Exception getException()
	{
		return got_exc;
	}
}
