package view.gui;

import controller.GraphicalUIFeatures;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PortfolioListPanel extends JPanel {
  private JComboBox<String> portfolioOptions;
  public String selected;
  public JButton selectedButton;

  public PortfolioListPanel() {
    super();
  }

  public void setupPortfolioList(String[] portfolios, GraphicalUIFeatures features) {
    if(portfolios.length == 0) {
      JLabel emptyList = new JLabel("No Existing Portfolios found, you can create one");
      add(emptyList);
    }
    else {
//      for (String pfID: portfolios
//      ) {
//        JButton thisPFID = new JButton(pfID);
//        thisPFID.setForeground(Color.BLUE.darker());
//        thisPFID.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        thisPFID.addActionListener( e -> this.features.getPortfolioInformation(pfID));
//        portfolioListPanel.add(thisPFID);
//
//      }
      add(new JLabel("Select One of The Portfolios"));
      portfolioOptions = new JComboBox<>(portfolios);
      portfolioOptions.addActionListener(e -> selected = (String) portfolioOptions.getSelectedItem());
      System.out.println(selected);
      selectedButton = new JButton("Show Portfolio Info");
      selectedButton.setActionCommand("Show Portfolio Info");

      add(portfolioOptions);
      add(selectedButton);
    }
    revalidate();
    repaint();
  }

}
