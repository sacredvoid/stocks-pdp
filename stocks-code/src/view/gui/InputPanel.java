package view.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

/**
 * The Input panel (bottom left) and stores all the buttons and methods to init those buttons.
 */
public class InputPanel extends JPanel {

  /**
   * The Create new portfolio button.
   */
  public JButton createNewPortfolio;
  /**
   * The flag to check if Jdialog button was pressed.
   */
  public int jdialogButtonPressed = -3;
  /**
   * The user data extracted from the JDialogPanes.
   */
  public String newPortfolioData;
  /**
   * The Buy stocks button.
   */
  public JButton buyStocks;
  /**
   * The Sell stocks button.
   */
  public JButton sellStocks;
  /**
   * The Add dollar cost button (this adds dollar cost to existing PF).
   */
  public JButton addDollarCost;
  /**
   * The Create sip button to create a new PF with DCA.
   */
  public JButton createSIP;
  /**
   * The Load external pf button to load external CSV into our app.
   */
  public JButton loadExternalPF;
  /**
   * The Set commission button to set commission for an app session.
   */
  public JButton setCommission;
  /**
   * The Commission value that was set.
   */
  public String commissionValue = "";
  /**
   * The Selected path of the CSV file when JFileChooser opens.
   */
  public String selectedPath;
  /**
   * The View strategies button which shows the current strategy running in a portfolio.
   */
  public JButton viewStrategies;
  /**
   * The Strategy input panel which lets user input a panel.
   */
  public StrategyInputPanel strategyInputPanel;

  /**
   * Instantiates a new Input panel.
   */
  public InputPanel() {
    super();
    setLayout(new GridLayout(0, 2, 30, 30));
    createNewPortfolio = new JButton("Create New Portfolio");
    createSIP = new JButton("Create New Dollar Cost Averaging (DCA) Portfolio");
    buyStocks = new JButton("Buy Stocks");
    sellStocks = new JButton("Sell Stocks");
    addDollarCost = new JButton("Add DCA Strategy");
    loadExternalPF = new JButton("Load External Portfolio");
    setCommission = new JButton("Set Commission Fees");
    viewStrategies = new JButton("View Current Strategies");

    addButtonsToTarget(this, new JComponent[]{createNewPortfolio, createSIP, buyStocks, sellStocks,
        addDollarCost, loadExternalPF, setCommission, viewStrategies});
  }

  /**
   * Create portfolio button dialog to accept user input for creating a new portfolio.
   */
  public void createPortfolioButtonDialog() {
    StockInputPanel s = new StockInputPanel("BUY", "Add Stocks To New Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  /**
   * Create buy stocks dialog to accept user input for buying stocks.
   */
  public void createBuyStocksDialog() {
    StockInputPanel s = new StockInputPanel("BUY", "Buy Stocks in selected Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  /**
   * Create sell stocks dialog to accept user input for selling stocks.
   */
  public void createSellStocksDialog() {
    StockInputPanel s = new StockInputPanel("SELL", "Sell Stocks in selected Portfolio");
    jdialogButtonPressed = s.jdialogButtonPressed;
    newPortfolioData = s.newPortfolioData;
  }

  /**
   * Create commission dialog to accept user input for setting commission.
   */
  public void createCommissionDialog() {
    commissionValue = JOptionPane.showInputDialog(
        "Enter Commission (only set for this session). Default: $1 per transaction.");
  }

  /**
   * Create load external pf dialog to accept user input to load the file into our app.
   */
  public void createLoadExternalPFDialog() {
    JFileChooser jFileChooser = new JFileChooser(
        FileSystemView.getFileSystemView().getHomeDirectory());
    jdialogButtonPressed = jFileChooser.showOpenDialog(null);
    if (jdialogButtonPressed == JFileChooser.APPROVE_OPTION) {
      selectedPath = jFileChooser.getSelectedFile().getAbsolutePath();
    }
  }

  /**
   * Create dca dialog to accept user input for creating a new DCA Strategy portfolio.
   */
  public void createDCADialog() {
    strategyInputPanel = new StrategyInputPanel("Create a new Portfolio with Strategy");
  }

  /**
   * Add dca to existing pf dialog to accept user input for adding DCA Strategy to existing
   * portfolio.
   */
  public void addDCAToExistingPFDialog() {
    strategyInputPanel = new StrategyInputPanel("Add a strategy to selected Portfolio");
  }

  private void addButtonsToTarget(JComponent target, JComponent[] buttons) {
    for (JComponent comp : buttons
    ) {
      target.add(comp);
    }
  }

}
