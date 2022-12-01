package view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;

public class test {
    public static void main(String[] args) throws Exception {
      JTextPane jtp = new JTextPane();
      Document doc = jtp.getDocument();
      for (int i = 0; i < 50; i++) {
        doc.insertString(doc.getLength(), " Hello Java World ", new SimpleAttributeSet());
        if ((3 == i) || (7 == i) || (15 == i)) {
          doc.insertString(doc.getLength(), " Hello Java World ", new SimpleAttributeSet());
          SimpleAttributeSet attrs = new SimpleAttributeSet();
          StyleConstants.setUnderline(attrs, true);
          StyleConstants.setForeground(attrs, Color.BLUE);
          String text = "www.google.com";
          URL url = new URL("http://" + text);
          attrs.addAttribute(HTML.Attribute.HREF, url.toString());
          doc.insertString(doc.getLength(), text, attrs);
        }
      }
      //JScrollPane jsp = new JScrollPane(jtp);
      //jsp.setPreferredSize(new Dimension(480, 150));
      //jsp.setBorder(null);
      jtp.setSize(new Dimension(480, 10));
      jtp.setPreferredSize(new Dimension(480, jtp.getPreferredSize().height));

      //JOptionPane.showMessageDialog(null, jsp, "Title", JOptionPane.INFORMATION_MESSAGE);
      JOptionPane.showMessageDialog(null, jtp, "Title", JOptionPane.INFORMATION_MESSAGE);
    }}
