package view.gui;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class InfoPanel extends JPanel {

  private JPanel datePickerPanel;
  private JPanel tablePanel;
  private JTable costBasisPanel;
  private JTable portfolioData;
  private JPanel cbData;
  private JPanel pfData;

  public InfoPanel() {
    super();
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    datePickerPanel = new JPanel();
    tablePanel = new JPanel();
    tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.PAGE_AXIS));
    // add these to the info panel which is a part of the mainpanel
    JButton datePickerButton = new JButton("Pick Date");
    datePickerButton.addActionListener(e -> setupCalendarPanel());
    add(datePickerButton);


    add(datePickerPanel);
    add(tablePanel);
    pfData = new JPanel(new BorderLayout());
    cbData = new JPanel(new BorderLayout());
    portfolioData = new JTable();
    costBasisPanel = new JTable();
    // Add the table to the table panel
    pfData.add(portfolioData);
    cbData.add(costBasisPanel);
    tablePanel.add(pfData);
    tablePanel.add(cbData);
  }

  public void setCostBasisData(String[] incomingData) {
    String[][] dataModel = new String[][]{incomingData};
    String[] columnNames = new String[] {"Total Amount Invested","Total Commission Charged","Total Amount+Commission","Total Earned by Selling"};
    setTableToPanel(cbData, costBasisPanel, dataModel, columnNames);
  }

  public void setPortfolioInformationTable(String pfInfo) {
    String[][] dataModel = convertCSVToTableModel(pfInfo);
    String[] columnNames = new String[] {"Stock","Quantity","Value"};
    setTableToPanel(pfData, portfolioData, dataModel, columnNames);
  }

  private void setTableToPanel(JPanel panel, JTable table, String[][] dataModel, String[] column) {
    panel.remove(table);
    JTable tempTable = new JTable(dataModel, column);
//    table = tempTable;
    table.getTableHeader().setReorderingAllowed(false);
    table.setDefaultEditor(Object.class, null);
    panel.add(tempTable, BorderLayout.CENTER);
    panel.add(tempTable.getTableHeader(), BorderLayout.NORTH);
    panel.revalidate();
    panel.repaint();
  }

  private String[][] convertCSVToTableModel(String csv) {
    String[] lines = csv.split("\n");
    String[][] data = new String[lines.length][];
    if(lines.length != 1) {
      for(int i=0;i<lines.length;i++) {
        data[i] = lines[i].split(",");
      }
    }
    return data;
  }

  private void setupCalendarPanel() {
//    datePicker = new JXDatePicker();
//    datePicker.setDate(Calendar.getInstance().getTime());
//    datePicker.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
//    datePickerPanel.add(datePicker);
  }


}
