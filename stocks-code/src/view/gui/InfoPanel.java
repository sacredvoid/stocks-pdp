package view.gui;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InfoPanel extends JPanel {
  private JPanel tablePanel;
  private JTable costBasisPanel;
  private JTable portfolioData;
  private JPanel cbData;
  private JPanel pfData;

  private final String[] costBasisColumns = new String[] {"Total Amount Invested","Total Commission Charged","Total Earned by Selling","Total Invested+Commission"};
  private final String[] portfolioDataColumns = new String[] {"Stock","Quantity","Value"};

  public InfoPanel() {
    super();
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    tablePanel = new JPanel();
    tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.PAGE_AXIS));
    add(tablePanel);
    pfData = new JPanel(new BorderLayout());
    cbData = new JPanel(new BorderLayout());
    portfolioData = new JTable();
    costBasisPanel = new JTable();
    // Add the table to the table panel
    pfData.add(portfolioData, BorderLayout.CENTER);
    cbData.add(costBasisPanel, BorderLayout.CENTER);
    tablePanel.add(pfData);
    tablePanel.add(cbData);
  }


  public void setCostBasisData(String[] incomingData) {
    String[][] dataModel = new String[][]{incomingData};
    setTableToPanel(cbData, costBasisPanel, dataModel, costBasisColumns);
  }

  public void setPortfolioInformationTable(String pfInfo) {
    String[][] dataModel = convertCSVToTableModel(pfInfo);
    setTableToPanel(pfData, portfolioData, dataModel, portfolioDataColumns);
  }

  private void setTableToPanel(JPanel panel, JTable table, String[][] dataModel, String[] column) {
//    panel.remove(table);
//    JTable tempTable = new JTable(dataModel, column);
//    table = tempTable;
    table.setModel(new DefaultTableModel(dataModel,column));
    table.getTableHeader().setReorderingAllowed(false);
    table.setDefaultEditor(Object.class, null);
    panel.add(table.getTableHeader(), BorderLayout.NORTH);
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
}
