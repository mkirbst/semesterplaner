package semesterplaner.libs;

/**
 * Nimmt Zeitabstaende in Millisekunden war.
 * 
 * @author Christian Vorberg
 */
public class TimeTaker 
{
	protected long start_time;
	protected long end_time;
	
	/**
	 * Stosst das Zeitnehmungsverfahren an.
	 */
	public void startTime()
	{
		end_time = -1;
		start_time = System.currentTimeMillis();
	}
	
	/**
	 * Stopt das Zeitnehmungsverfahren.
	 */
	public void endTime()
	{
		end_time = System.currentTimeMillis();
	}
	
	/**
	 * Hohlt die gestoppte Zeit. 
	 * @return Die gestoppte Zeit. -1, wenn die Zeitgebung noch nicht beendet ist.
	 */
	public long getTime()
	{
		if (end_time == -1)
			return -1;
		
		return end_time - start_time;
	}
	
	/**
	 * Konvertiert die genommene Zeit in einen String.
	 * Wenn > 1000, werden Sekunden zurueckgegeben.
	 * 
	 * return Konvertierte Zeit
	 */
	public String toString()
	{
		if (getTime() > 1000)
			return Double.toString((double) getTime() / (double) 1000) + " Sekunden";
		else
			return Long.toString(getTime()) + " Millisekunden";
	}
}
