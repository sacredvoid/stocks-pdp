package view.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

public class StockInputPanel extends JPanel {

  // Create the panel to show in JOptionDialog

  public int jdialogButtonPressed;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public String newPortfolioData;

  public StockInputPanel() {
    super();
    JTextField stockName = new JTextField();
    JTextField stockQuantity = new JTextField();
    JXDatePicker transactionDate = new JXDatePicker(new Date());
    transactionDate.setFormats(simpleDateFormat);
    Object[] dialog = {
        "Stock Name: ", stockName,
        "Stock Quantity: ", stockQuantity,
        "Buy Date: ", transactionDate
    };

    jdialogButtonPressed = JOptionPane.showConfirmDialog(getParent(), dialog, "Create New Portfolio", JOptionPane.OK_CANCEL_OPTION);
    StringBuilder csvData = new StringBuilder();
    csvData.append(stockName.getText()).append(",").append(stockQuantity.getText()).append(",").append(simpleDateFormat.format(transactionDate.getDate()));
    newPortfolioData = csvData.toString();
  }

}
