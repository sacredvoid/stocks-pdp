package view.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class InputPanel extends JPanel {

  public JButton createNewPortfolio;
  public int jdialogButtonPressed = -3;
  public String newPortfolioData;
  public JButton buyStocks;
  public JButton sellStocks;
  public JButton dollarCost;
  public JButton createSIP;
  public JButton loadExternalPF;
  public JButton setCommission;
  public String commissionValue = "";
  public String selectedPath;

  public InputPanel() {
    super();
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    createNewPortfolio = new JButton("Create New Portfolio");
    createSIP = new JButton("Create New SIP");
    buyStocks = new JButton("Buy Stocks");
    sellStocks = new JButton("Sell Stocks");
    dollarCost = new JButton("Add Dollar-Cost Averaging Strategy");
    loadExternalPF = new JButton("Load External Portfolio");
    setCommission = new JButton("Set Commission Fees");
    // TODO Give error message to user if sold more/on a weekend

    addButtonsToTarget(this,new JComponent[]{createNewPortfolio,createSIP,buyStocks,sellStocks,dollarCost, loadExternalPF, setCommission});
  }

  public void createPortfolioButtonDialog() {
    StockInputPanel s = new StockInputPanel("BUY", "Add Stocks To New Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  public void createBuyStocksDialog() {
    StockInputPanel s = new StockInputPanel("BUY", "Buy Stocks in selected Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  public void createSellStocksDialog() {
    StockInputPanel s = new StockInputPanel("SELL", "Sell Stocks in selected Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  public void createCommissionDialog() {
    commissionValue = JOptionPane.showInputDialog("Enter Commission (only set for this session). Default: $1 per transaction.");
  }

  public void createLoadExternalPFDialog() {
    JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jdialogButtonPressed = jFileChooser.showOpenDialog(null);
    if(jdialogButtonPressed == JFileChooser.APPROVE_OPTION) {
      selectedPath = jFileChooser.getSelectedFile().getAbsolutePath();
    }
  }

  public void createDCADialog() {
    StrategyInputPanel strategyInputPanel = new StrategyInputPanel("Create a new Portfolio with Strategy");
  }

  private void addButtonsToTarget(JComponent target, JComponent[] buttons) {
    for (JComponent comp: buttons
    ) {
      target.add(comp);
    }
  }

}
