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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

public class StockInputPanel {
  public JPanel tempPanel;
  public int jdialogButtonPressed;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public String newPortfolioData;

  private String operation;
  public JPanel listOfInputPanels;
  public JPanel mainPanel;
  public JPanel addButtonPanel;
  public JScrollPane scrollListOfInputs;


  public StockInputPanel(String operation, String title) {
    super();
    this.operation = operation;
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
    addButtonPanel = new JPanel();
    listOfInputPanels = new JPanel();
    listOfInputPanels.setLayout(new GridLayout(0,1,2,2));
    scrollListOfInputs = new JScrollPane(listOfInputPanels);
    mainPanel.add(scrollListOfInputs);
    mainPanel.add(addButtonPanel);
    newInputPanel();
    JButton addMoreInput = new JButton("+");
    mainPanel.setSize(480,10);
    mainPanel.setSize(480,mainPanel.getPreferredSize().height);
//    JButton subtractInput = new JButton("-");
    addMoreInput.addActionListener(e -> {
      int selected = JOptionPane.showConfirmDialog(null,"You want to enter another transaction?","Confirm",JOptionPane.YES_NO_OPTION);
      if(selected == JOptionPane.YES_OPTION) {
        newInputPanel();
      }
    });
    addButtonPanel.add(addMoreInput);

    jdialogButtonPressed = JOptionPane.showConfirmDialog(null, mainPanel, title, JOptionPane.OK_CANCEL_OPTION);
    if(jdialogButtonPressed == JOptionPane.OK_OPTION) {
      newPortfolioData = extractData();
    }
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
