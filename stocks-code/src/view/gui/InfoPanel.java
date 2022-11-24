package view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

public class InfoPanel extends JPanel {

  private JPanel datePickerPanel;
  private JPanel tablePanel;
  private JTable portfolioData;

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
    portfolioData = new JTable();
    // Add the table to the table panel
    tablePanel.add(portfolioData);
  }

  public void setPortfolioInformationTable(String pfInfo) {
    String[][] dataModel = convertCSVToTableModel(pfInfo);
    String[] columnNames = new String[] {"Stock","Quantity","Value"};
    tablePanel.remove(portfolioData);
    portfolioData = new JTable(dataModel,columnNames);
    portfolioData.setDefaultEditor(Object.class,null);
    tablePanel.add(portfolioData);
    tablePanel.revalidate();
    tablePanel.repaint();
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
