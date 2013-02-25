package semesterplaner.libs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Entfernt aus einer eingelesenen Datei alle Leerzeilen.
 *
 * @author Christian Vorberg
 */
public class RemoveEmptyLinesInputReader extends Reader
{
        protected Reader in;

	/**
	 * Das nexte Byte welches Ausgelesen werden soll
	 */
	protected int nextbyte = -1;
	/**
	 * Das letzte Byte, um zu ueberpruefen ob es eine Leerzeile war.
	 */
	protected int lastbyte = -1;
	/**
	 * Eine Temporaere Variable, die Max. 4 Bytes enthalten kann.
	 */
	protected int temp;
	
	protected boolean first;

	/**
	 * Erzeugt einen FilterInputStream, welcher Leerzeilen (\n oder \r\n) ueberliest.
	 * @param in Der Uebergeordnete InputStream
	 */
	public RemoveEmptyLinesInputReader(Reader in)
	{
		this.in = in;
		first = true;
		//temp = new int[4];
	}
	
        /**
         * Liest das naechste aus dem angegebenen InputStream. Leerzeilen werden dabei ueberlesen.
         *
         * @return     Das naechste DatenByte, oder -1, wenn der Stream zuende ist.
         * @exception  IOException  wenn ein I/O Error auftritt.
         * @author Christian Vorberg
         */
	@Override
	public int read() throws IOException 
	{
		nextbyte = in.read();

		if (nextbyte == -1 && lastbyte == -1) //Keine Bytes mehr?
			return -1;
		else if (nextbyte == -1 && lastbyte != -1) //Noch eins zu schicken?
		{
			temp = lastbyte;
			lastbyte = -1;
			return temp;
		}
		
		while (true)
		{
			if (nextbyte == '\r' || nextbyte == '\n') //\r impliziert \n
			{
				if (first) 
				{
					//Das erste \n ausgeben!
					first = false;
					return '\n'; //Scheiss auf \r!
				}
				
				if (nextbyte == '\r') //bei \r eins (\n) ueberlesen
				{
					nextbyte = in.read();
					if (nextbyte == -1) return -1;
				}
								
				nextbyte = in.read(); //Das naechste Zeichen holen
				if (nextbyte == -1)
					return -1;
			}
			else break;
		}
		
		//Wenn wir hier herauskommen, haben wir kein \n.
		if (nextbyte == '\n' || nextbyte == '\r')
			throw new IOException("(Debug Message) FAIL! Slash N!");
		
		first = true;
		return nextbyte;
	}

        /**
	 * Total Useless. Use read(). Same Function.
	 */
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int I = read();
            if (I == -1)
                return -1;

            cbuf[off] = (char) I;
            return 1;
        }

        /**
         * Schliesst den uebergebenen Inputstream.
         * @throws IOException
         */
        @Override
        public void close() throws IOException {
            in.close();
        }
}
