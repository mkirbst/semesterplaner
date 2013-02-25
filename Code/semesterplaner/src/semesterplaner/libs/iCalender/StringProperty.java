/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package semesterplaner.libs.iCalender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;

/**
 * Ein Parameter für iCal4j, welcher Dynamisch Werte annehmen kann.
 * @author christian
 */
public class StringProperty extends Property
{
    String value;

    /**
     * Erzeugt den Parameter
     * @param Property Name des Parameters
     * @param value Wert
     */
    public StringProperty(String Property,String value)
    {
        super(Property,null);
        this.value = value;
    }

    /**
     * Setzt den Wert des Parameters
     * @param aValue
     * @throws IOException unused
     * @throws URISyntaxException unused
     * @throws ParseException unused
     */
    @Override
    public void setValue(String aValue) throws IOException, URISyntaxException, ParseException {
        value = aValue;
    }

    /**
     * Funktion hat keine Implementierung.
     * @throws ValidationException unused
     */
    @Override
    public void validate() throws ValidationException {
        
    }

    /**
     * Hohlt den Wert des Parameters.
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }

}
