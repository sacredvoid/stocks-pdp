package view.gui.inputverifiers;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class stockVerifier extends InputVerifier {



  @Override
  public boolean verify(JComponent input) {
    String text = ((JTextField) input).getText().trim();
    TitledBorder border = (TitledBorder) input.getBorder();
    if(text.matches("([A-Z])+")) {
      border.setTitleColor(Color.BLACK);
      input.repaint();
      return true;
    }
    else {
      border.setTitleColor(Color.RED);
      input.repaint();
      return false;
    }
  }
}
