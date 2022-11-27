package view.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

public class StockInputPanel {
  public JPanel tempPanel;
  public int jdialogButtonPressed;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public String newPortfolioData;

  private String operation;
  public JPanel inputPanel;
  public JPanel listOfInputPanels;
  public JPanel mainPanel;
  public JPanel addButtonPanel;


  public StockInputPanel(String operation) {
    super();
    this.operation = operation;
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
    addButtonPanel = new JPanel();
    inputPanel = new JPanel();
    listOfInputPanels = new JPanel();
    listOfInputPanels.add(inputPanel);
    mainPanel.add(listOfInputPanels);
    mainPanel.add(addButtonPanel);
    newInputPanel();
    JButton addMoreInput = new JButton("+");
//    JButton subtractInput = new JButton("-");
    addMoreInput.addActionListener(e -> {
      newInputPanel();
    });
    addButtonPanel.add(addMoreInput);

    jdialogButtonPressed = JOptionPane.showConfirmDialog(null, mainPanel, "Create New Portfolio", JOptionPane.OK_CANCEL_OPTION);
//    StringBuilder csvData = new StringBuilder();
//    csvData.append(stockName.getText()).append(",").append(stockQuantity.getText()).append(",").append(simpleDateFormat.format(transactionDate.getDate()));
//    newPortfolioData = csvData.toString();
    newPortfolioData = extractData();
    System.out.println(newPortfolioData);
  }

  private void newInputPanel() {
    tempPanel = new JPanel();
    tempPanel.setLayout(new GridLayout(0,3,2,2));
    JTextField stockName = new JTextField();
    stockName.setBorder(BorderFactory.createTitledBorder("Stock Name"));
    JTextField stockQuantity = new JTextField();
    stockQuantity.setBorder(BorderFactory.createTitledBorder("Stock Quantity"));
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
    for (Component c: listOfInputPanels.getComponents()
    ) {
      StringJoiner line = new StringJoiner(",");
      for(Component subC: ((JPanel) c).getComponents()) {
        if(subC instanceof JTextField) {
          line.add(((JTextField) subC).getText());
        }
        if (subC instanceof JXDatePicker) {
          line.add(simpleDateFormat.format(((JXDatePicker) subC).getDate()));
        }
      }
      if(line.length() == 0) {
        continue;
      }
      data.append(line).append(",").append(operation).append("\n");
    }
    return data.toString().strip();
  }
}
