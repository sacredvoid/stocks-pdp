package view.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class StrategyInputPanel {
  public JPanel tempPanel;
  public int jdialogButtonPressed;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public String newPortfolioData;

  public JPanel listOfInputPanels;
  public JPanel mainPanel;
  public JPanel addButtonPanel;
  public JScrollPane scrollListOfInputs;
  public JPanel DCAInputsPanel;
  public int numberOfInputs = 1;
  public JTextField recurringAmount;
  public JXDatePicker startDate;
  public JXDatePicker endDate;


  public StrategyInputPanel(String title) {
    super();
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
    addButtonPanel = new JPanel();
    // DCA Inputs
    DCAInputsPanel = new JPanel();
    DCAInputsPanel.setLayout(new BoxLayout(DCAInputsPanel,BoxLayout.PAGE_AXIS));
    recurringAmount = new JTextField();
    recurringAmount.setBorder(BorderFactory.createTitledBorder("Amount to Invest Monthly"));
    startDate = new JXDatePicker();
    startDate.setBorder(BorderFactory.createTitledBorder("Strategy Start Date"));
    startDate.setFormats(simpleDateFormat);
    endDate = new JXDatePicker();
    endDate.setBorder(BorderFactory.createTitledBorder("Strategy End Date"));
    endDate.setFormats(simpleDateFormat);
    DCAInputsPanel.add(recurringAmount);
    DCAInputsPanel.add(startDate);
    DCAInputsPanel.add(endDate);

    // Stocks and Weightage inputs
    listOfInputPanels = new JPanel();
    listOfInputPanels.setLayout(new GridLayout(0,1,2,2));
    scrollListOfInputs = new JScrollPane(listOfInputPanels);
    mainPanel.add(DCAInputsPanel);
    mainPanel.add(scrollListOfInputs);
    mainPanel.add(addButtonPanel);
    newInputPanel();
    JButton addMoreInput = new JButton("+");
    mainPanel.setSize(480,10);
    mainPanel.setSize(480,mainPanel.getPreferredSize().height);
//    JButton subtractInput = new JButton("-");
    addMoreInput.addActionListener(e -> {
      int selected = JOptionPane.showConfirmDialog(null,"You want to enter another stock percentage?","Confirm",JOptionPane.YES_NO_OPTION);
      if(selected == JOptionPane.YES_OPTION) {
        // Calculate next percentages
        newInputPanel();
      }
    });
    addButtonPanel.add(addMoreInput);

    jdialogButtonPressed = JOptionPane.showConfirmDialog(null, mainPanel, title, JOptionPane.OK_CANCEL_OPTION);
    if(jdialogButtonPressed == JOptionPane.OK_OPTION) {
      newPortfolioData = extractData(DCAInputsPanel);
      newPortfolioData = newPortfolioData + "\n"+ extractData(listOfInputPanels);
      System.out.println(newPortfolioData);
    }
  }

  private void newInputPanel() {
    tempPanel = new JPanel();
    tempPanel.setLayout(new GridLayout(0,2,2,2));
    JTextField stockName = new JTextField();
    stockName.setBorder(BorderFactory.createTitledBorder("Stock Name"));
    JTextField stockQuantity = new JTextField();
    stockQuantity.setBorder(BorderFactory.createTitledBorder("Stock Weightage"));
    tempPanel.add(stockName);
    tempPanel.add(stockQuantity);
    listOfInputPanels.add(tempPanel);
    listOfInputPanels.revalidate();
    listOfInputPanels.repaint();
  }
  private String extractData(JComponent parent) {
    StringBuilder data = new StringBuilder();
    for (Component c: parent.getComponents()
    ) {
      StringJoiner line = new StringJoiner(",");
      if(c instanceof JTextField) {
        line.add(((JTextField) c).getText());
      } else if (c instanceof JXDatePicker) {
          line.add(simpleDateFormat.format(((JXDatePicker) c).getDate()));
      }
      else {
        for(Component subC: ((JPanel) c).getComponents()) {
          if(subC instanceof JTextField) {
            line.add(((JTextField) subC).getText());
          }
        }
      }
      if(line.length() == 0) {
        continue;
      }
      data.append(line).append("\n");
    }
    return data.toString().strip();
  }
}
