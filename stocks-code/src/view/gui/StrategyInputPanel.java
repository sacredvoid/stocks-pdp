package view.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;
import view.gui.inputverifiers.NameVerifier;
import view.gui.inputverifiers.QuantityVerifier;
import view.gui.inputverifiers.StockVerifier;
import view.gui.inputverifiers.WeightageVerifier;

/**
 * The panel which holds input fields for a strategy related information.
 */
public class StrategyInputPanel {

  /**
   * The Temp panel which is actually a panel to hold JTextFields of Stock, Weightage.
   */
  public JPanel tempPanel;
  /**
   * The Jdialog button pressed to track the state and decide flow.
   */
  public int jdialogButtonPressed;
  /**
   * The Simple date format used by JXDatePicker.
   */
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  /**
   * The New portfolio data as a CSV coming from the user.
   */
  public String newPortfolioData;

  /**
   * The List of input panels which contain multiple temp panels.
   */
  public JPanel listOfInputPanels;
  /**
   * The Main panel which holds all of the described panels.
   */
  public JPanel mainPanel;
  /**
   * The Add button panel to let user add more stocks.
   */
  public JPanel addButtonPanel;
  /**
   * The Scroll list of inputs which holds the listOfInputPanels.
   */
  public JScrollPane scrollListOfInputs;
  /**
   * The Dca inputs panel which holds the JTextFields pertaining to DCA Strategy details like name,
   * cycle, value, start/end dates.
   */
  public JPanel dcaInputsPanel;
  /**
   * The Recurring amount for strategy.
   */
  public JTextField recurringAmount;
  /**
   * The Start date of strategy.
   */
  public JXDatePicker startDate;
  /**
   * The End date of strategy.
   */
  public JXDatePicker endDate;
  /**
   * The Recurring cycle for the strategy.
   */
  public JTextField recurringCycle;
  /**
   * The DCA strategy name.
   */
  public JTextField dcaName;


  /**
   * Instantiates a new Strategy input panel.
   *
   * @param title the title of the JOptionPane
   */
  public StrategyInputPanel(String title) {
    super();
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    addButtonPanel = new JPanel();
    // DCA Inputs
    dcaInputsPanel = new JPanel();
    dcaInputsPanel.setLayout(new GridLayout(0, 1, 2, 2));
    dcaName = new JTextField();
    dcaName.setBorder(BorderFactory.createTitledBorder("DCA Strategy Name"));
    dcaName.setInputVerifier(new NameVerifier());
    recurringCycle = new JTextField();
    recurringCycle.setBorder(BorderFactory.createTitledBorder("Recurring Cycle (Days)"));
    recurringCycle.setInputVerifier(new QuantityVerifier());
    recurringAmount = new JTextField();
    recurringAmount.setBorder(BorderFactory.createTitledBorder("Amount to Invest Every Cycle"));
    recurringAmount.setInputVerifier(new QuantityVerifier());
    startDate = new JXDatePicker();
    startDate.setBorder(BorderFactory.createTitledBorder("Strategy Start Date"));
    startDate.setFormats(simpleDateFormat);
    endDate = new JXDatePicker();
    endDate.setBorder(BorderFactory.createTitledBorder("Strategy End Date"));
    endDate.setFormats(simpleDateFormat);
    dcaInputsPanel.add(dcaName);
    dcaInputsPanel.add(recurringCycle);
    dcaInputsPanel.add(recurringAmount);
    dcaInputsPanel.add(startDate);
    dcaInputsPanel.add(endDate);

    // Stocks and Weightage inputs
    listOfInputPanels = new JPanel();
    listOfInputPanels.setLayout(new GridLayout(0, 1, 2, 2));
    scrollListOfInputs = new JScrollPane(listOfInputPanels);
    mainPanel.add(dcaInputsPanel);
    mainPanel.add(scrollListOfInputs);
    mainPanel.add(addButtonPanel);
    newInputPanel();
    // TODO Get the dynamic weightage calculation done
    JButton addMoreInput = new JButton("+");
    mainPanel.setSize(480, 10);
    mainPanel.setSize(480, mainPanel.getPreferredSize().height);
    addMoreInput.addActionListener(e -> {
      int selected = JOptionPane.showConfirmDialog(null,
          "You want to enter another stock percentage?",
          "Confirm", JOptionPane.YES_NO_OPTION);
      if (selected == JOptionPane.YES_OPTION) {
        // Calculate next percentages
        newInputPanel();
      }
    });
    addButtonPanel.add(addMoreInput);

    jdialogButtonPressed = JOptionPane.showConfirmDialog(null, mainPanel, title,
        JOptionPane.OK_CANCEL_OPTION);
    if (jdialogButtonPressed == JOptionPane.OK_OPTION) {
      newPortfolioData = extractData(dcaInputsPanel);
      newPortfolioData = newPortfolioData + "\n" + extractData(listOfInputPanels);
    }
  }

  private void newInputPanel() {
    tempPanel = new JPanel();
    tempPanel.setLayout(new GridLayout(0, 2, 2, 2));
    JTextField stockName = new JTextField();
    stockName.setBorder(BorderFactory.createTitledBorder("Stock Name"));
    stockName.setInputVerifier(new StockVerifier());
    JTextField stockQuantity = new JTextField();
    stockQuantity.setBorder(BorderFactory.createTitledBorder("Stock Weightage"));
    stockQuantity.setInputVerifier(new WeightageVerifier());
    tempPanel.add(stockName);
    tempPanel.add(stockQuantity);
    listOfInputPanels.add(tempPanel);
    listOfInputPanels.revalidate();
    listOfInputPanels.repaint();
  }

  private String extractData(JComponent parent) {
    StringBuilder data = new StringBuilder();
    for (Component c : parent.getComponents()) {
      StringJoiner line = new StringJoiner(",");
      if (c instanceof JTextField) {
        line.add(((JTextField) c).getText());
      } else if (c instanceof JXDatePicker) {
        if (((JXDatePicker) c).getDate() != null) {
          line.add(simpleDateFormat.format(((JXDatePicker) c).getDate()));
        } else {
          line.add("null");
        }
      } else {
        for (Component subC : ((JPanel) c).getComponents()) {
          if (subC instanceof JTextField) {
            line.add(((JTextField) subC).getText());
          }
        }
      }
      if (line.length() == 0) {
        continue;
      }
      data.append(line).append("\n");
    }
    return data.toString().strip();
  }

}
