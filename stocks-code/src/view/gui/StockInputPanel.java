package view.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;
import view.gui.inputverifiers.QuantityVerifier;
import view.gui.inputverifiers.StockVerifier;

/**
 * The panel that lets user input Stock,Quantity,Date in the JOptionPane when you click on
 * Buy/Sell.
 */
public class StockInputPanel {

  /**
   * A temp panel which actually stores the JTextFields for Stock,Qt,Date and depending on user
   * input, more are appended to the existing panel.
   */
  public JPanel tempPanel;
  /**
   * The Jdialog button pressed to track the status of a Jdialogbutton that was pressed.
   */
  public int jdialogButtonPressed;
  /**
   * The Simple date format to format JXDatePicker format.
   */
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  /**
   * The New portfolio data coming from the user (Stock,Qt,Date) as a CSV.
   */
  public String newPortfolioData;

  private String operation;
  /**
   * The List of input panels which contains a list of temp panels and this list keeps growing
   * depending on how many ever stocks user wants to add.
   */
  public JPanel listOfInputPanels;
  /**
   * The Main panel which is passed to the JOptionPane and contains everything.
   */
  public JPanel mainPanel;
  /**
   * The Add button panel which lets user add more stocks if needed.
   */
  public JPanel addButtonPanel;
  /**
   * The Scroll list of inputs which holds the listOfInputPanels and adds scroll feature.
   */
  public JScrollPane scrollListOfInputs;


  /**
   * Instantiates a new Stock input panel.
   *
   * @param operation the operation (buy/sell)
   * @param title     the title of the JOptionPane
   */
  public StockInputPanel(String operation, String title) {
    super();
    this.operation = operation;
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    addButtonPanel = new JPanel();
    listOfInputPanels = new JPanel();
    listOfInputPanels.setLayout(new GridLayout(0, 1, 2, 2));
    scrollListOfInputs = new JScrollPane(listOfInputPanels);
    mainPanel.add(scrollListOfInputs);
    mainPanel.add(addButtonPanel);
    newInputPanel();
    JButton addMoreInput = new JButton("+");
    mainPanel.setSize(480, 10);
    mainPanel.setSize(480, mainPanel.getPreferredSize().height);
    addMoreInput.addActionListener(e -> {
      int selected = JOptionPane.showConfirmDialog(null,
          "You want to enter another transaction?",
          "Confirm", JOptionPane.YES_NO_OPTION);
      if (selected == JOptionPane.YES_OPTION) {
        newInputPanel();
      }
    });
    addButtonPanel.add(addMoreInput);

    jdialogButtonPressed = JOptionPane.showConfirmDialog(null, mainPanel, title,
        JOptionPane.OK_CANCEL_OPTION);
    if (jdialogButtonPressed == JOptionPane.OK_OPTION) {
      newPortfolioData = extractData();
    }
  }

  private void newInputPanel() {
    tempPanel = new JPanel();
    tempPanel.setLayout(new GridLayout(0, 3, 2, 2));
    JTextField stockName = new JTextField();
    stockName.setBorder(BorderFactory.createTitledBorder("Stock Name"));
    stockName.setInputVerifier(new StockVerifier());
    JTextField stockQuantity = new JTextField();
    stockQuantity.setBorder(BorderFactory.createTitledBorder("Stock Quantity"));
    stockQuantity.setInputVerifier(new QuantityVerifier());
    JXDatePicker transactionDate = new JXDatePicker(new Date());
    transactionDate.setBorder(BorderFactory.createTitledBorder("Date"));
    transactionDate.getMonthView().setUpperBound(new Date());
    transactionDate.setFormats(simpleDateFormat);
    tempPanel.add(stockName);
    tempPanel.add(stockQuantity);
    tempPanel.add(transactionDate);
    listOfInputPanels.add(tempPanel);
    listOfInputPanels.revalidate();
    listOfInputPanels.repaint();
  }

  private String extractData() {
    StringBuilder data = new StringBuilder();
    for (Component c : listOfInputPanels.getComponents()
    ) {
      StringJoiner line = new StringJoiner(",");
      for (Component subC : ((JPanel) c).getComponents()) {
        if (subC instanceof JTextField) {
          line.add(((JTextField) subC).getText());
        }
        if (subC instanceof JXDatePicker) {
          line.add(simpleDateFormat.format(((JXDatePicker) subC).getDate()));
        }
      }
      if (line.length() == 0) {
        continue;
      }
      data.append(line).append(",").append(operation).append("\n");
    }
    return data.toString().strip();
  }
}
