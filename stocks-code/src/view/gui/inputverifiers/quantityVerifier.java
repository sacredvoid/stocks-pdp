package view.gui.inputverifiers;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * The nameVerifier that checks the JTextField input for quantity (any non-zero, positive number).
 */
public class quantityVerifier extends InputVerifier {

  @Override
  public boolean verify(JComponent input) {
    String text = ((JTextField) input).getText().trim();
    TitledBorder titledBorder = (TitledBorder) input.getBorder();
    if (text.matches("^[1-9]\\d*$")) {
      titledBorder.setTitleColor(Color.black);
      input.repaint();
      return true;
    } else {
      titledBorder.setTitleColor(Color.red);
      input.repaint();
      return false;
    }
  }
}
