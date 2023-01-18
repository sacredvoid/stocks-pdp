package view.gui.inputverifiers;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * The type Weightage verifier that checks the JTextField input for non-zero positive float values.
 */
public class WeightageVerifier extends InputVerifier {

  @Override
  public boolean verify(JComponent input) {
    String text = ((JTextField) input).getText().trim();
    TitledBorder titledBorder = (TitledBorder) input.getBorder();
    if (text.matches("^\\s*(?=.*[1-9])\\d*(?:\\.\\d{1,2})?\\s*$")) {
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
