package view.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

public class InputPanel extends JPanel {

  public JButton createNewPortfolio;
  public int createPortfolioPressed = -3;
  public String newPortfolioData;
  public JButton buyStocks;
  public JButton sellStocks;
  public JButton dollarCost;
  public JButton createSIP;

  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public InputPanel() {
    super();
    createNewPortfolio = new JButton("Create New Portfolio");
    createSIP = new JButton("Create New SIP");
    buyStocks = new JButton("Buy Stocks");
    sellStocks = new JButton("Sell Stocks");
    dollarCost = new JButton("Add Dollar-Cost Averaging Strategy");
    addButtonsToTarget(this,new JComponent[]{createNewPortfolio,createSIP,buyStocks,sellStocks,dollarCost});
  }

  public void createPortfolioButtonDialog() {
    JTextField stockName = new JTextField();
    JTextField stockQuantity = new JTextField();
    JXDatePicker transactionDate = new JXDatePicker(new Date());
    transactionDate.setFormats(simpleDateFormat);
    Object[] dialog = {
        "Stock Name: ", stockName,
        "Stock Quantity: ", stockQuantity,
        "Buy Date: ", transactionDate
    };

    createPortfolioPressed = JOptionPane.showConfirmDialog(this, dialog, "Create New Portfolio", JOptionPane.OK_CANCEL_OPTION);
    StringBuilder csvData = new StringBuilder();
    csvData.append(stockName.getText()).append(",").append(stockQuantity.getText()).append(",").append(simpleDateFormat.format(transactionDate.getDate())).append(",BUY");
    newPortfolioData = csvData.toString();
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
