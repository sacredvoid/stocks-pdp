package view.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class InputPanel extends JPanel {

  public JButton createNewPortfolio;
  public JButton buyStocks;
  public JButton sellStocks;
  public JButton dollarCost;
  public JButton createSIP;
  public InputPanel() {
    super();
    createNewPortfolio = new JButton("Create New Portfolio");
    createSIP = new JButton("Create New SIP");
    buyStocks = new JButton("Buy Stocks");
    sellStocks = new JButton("Sell Stocks");
    dollarCost = new JButton("Add Dollar-Cost Averaging Strategy");
    addButtonsToTarget(this,new JComponent[]{createNewPortfolio,createSIP,buyStocks,sellStocks,dollarCost});
  }

  private void addButtonsToTarget(JComponent target, JComponent[] buttons) {
    for (JComponent comp: buttons
    ) {
      target.add(comp);
    }
  }

  private void openJOptionDialog() {
    // TODO General purpose function which takes in list of required inputs and creates a pop-up box
  }

}
