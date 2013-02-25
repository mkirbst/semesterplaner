/*
 * (swing1.1beta3)
 * 
 */

package semesterplaner.libs.SplitTable;

import java.awt.*;


/**
 * Quelle: http://www.codeguru.com/java/articles/139.shtml
 * @version 1.0 11/22/98
 */

public interface CellFont {
  
  public Font getFont(int row, int column);
  public void setFont(Font font, int row, int column);
  public void setFont(Font font, int[] rows, int[] columns);


}
