package view.gui;

import controller.GraphicalUIFeatures;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.Orchestrator;
import modelview.IModelView;
import modelview.ModelView;
import org.jfree.chart.ui.UIUtils;

public class JFrameView extends JFrame {

  private JPanel mainPanel;
  private PortfolioListPanel portfolioPanel;
  private String[] portfolioList = new String[]{};
  private GraphPanel graphPanel;
  private InputPanel inputPanel;
  private JScrollPane mainScrollPane;
  private InfoPanel infoPanel;
  private GraphicalUIFeatures features;

  private IModelView modelView;

  private JFrameView swingUI;

  public JFrameView(Orchestrator model) {
    super();
    this.modelView = new ModelView(model);
    this.swingUI = this;
  }

  private void setupMainJFrame() {
    setTitle("Aakasam Stock Trading");
    setSize(0,0);
    setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
    setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
    this.toFront();
    this.repaint();
    //TODO Test different layouts
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(0,2, -1, -1));
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);
  }

  private void setupStockListPanel() {
    portfolioPanel = new PortfolioListPanel();
    portfolioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainPanel.add(portfolioPanel);
  }

  public void populateStockList() {
    portfolioList = modelView.getExistingPortfolios();
    portfolioPanel.setupPortfolioList(this.portfolioList);
  }

  public void setFeatures(GraphicalUIFeatures features) {
    this.features = features;
    // Get portfolio composition and cost-basis for given date
    portfolioPanel.selectedButton.addActionListener(e -> {
      features.getPortfolioInformation(portfolioPanel.selected, portfolioPanel.selectedDateString);
      features.getCostBasis(portfolioPanel.selected, portfolioPanel.selectedDateString);
    });

    // Create new portfolio
    inputPanel.createNewPortfolio.addActionListener(e -> {
      inputPanel.createPortfolioButtonDialog();
      if(inputPanel.jdialogButtonPressed != -3) {
        if(inputPanel.jdialogButtonPressed == JOptionPane.OK_OPTION) {
          features.createPortfolio(inputPanel.newPortfolioData);
          portfolioPanel.updatePortfolioList(modelView.getExistingPortfolios());
        }
      }
    });

    // Edit existing (BUY) portfolio
    inputPanel.buyStocks.addActionListener(e -> {
      inputPanel.createBuyStocksDialog();
      executeBuySell(features);
    });

    // Edit existing (SELL) portfolio
    inputPanel.sellStocks.addActionListener(e -> {
      inputPanel.createSellStocksDialog();
      executeBuySell(features);
    });

    // Set commission
    inputPanel.setCommission.addActionListener(e -> {
      inputPanel.createCommissionDialog();
      if(inputPanel.commissionValue !=  null) {
        this.features.setCommission(inputPanel.commissionValue);
      }
    });

    // Load external portfolio
    inputPanel.loadExternalPF.addActionListener(e -> {
      inputPanel.createLoadExternalPFDialog();
      if(inputPanel.jdialogButtonPressed == JFileChooser.APPROVE_OPTION) {
        this.features.loadExternalPortfolio(inputPanel.selectedPath);
        updateInformation();
      }
    });

    // Add DCA
    inputPanel.createSIP.addActionListener(e -> {
      inputPanel.createDCADialog();
      if(!inputPanel.strategyInputPanel.newPortfolioData.isEmpty()) {
        this.features.createDCAPortfolio(inputPanel.strategyInputPanel.newPortfolioData);
        updateInformation();
      }
    });

    graphPanel.showGraph.addActionListener(e -> {
      graphPanel.addGraph(features.getChart(portfolioPanel.selected, graphPanel.startDateString, graphPanel.endDateString));
    });

    // Add DCA to existing portfolio
    inputPanel.addDollarCost.addActionListener(e -> {
      inputPanel.addDCAToExistingPFDialog();
      if(!inputPanel.strategyInputPanel.newPortfolioData.isEmpty()) {
        this.features.addDCAToExistingPF(portfolioPanel.selected,inputPanel.strategyInputPanel.newPortfolioData);
        updateInformation();
      }
    });

  }

  private void executeBuySell(GraphicalUIFeatures features) {
    if(inputPanel.jdialogButtonPressed != -3) {
      if(inputPanel.jdialogButtonPressed == JOptionPane.OK_OPTION) {
        features.modifyPortfolio(portfolioPanel.selected, inputPanel.newPortfolioData);
        updateInformation();
      }
    }
  }

  public void updateInformation() {
    portfolioPanel.updatePortfolioList(modelView.getExistingPortfolios());
    features.getPortfolioInformation(portfolioPanel.selected, portfolioPanel.selectedDateString);
    features.getCostBasis(portfolioPanel.selected, portfolioPanel.selectedDateString);
  }

  private void setupPortfolioInfoPanel() {
    infoPanel = new InfoPanel();
    infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainPanel.add(infoPanel);
    // Create date and table panel
  }

  public void setInfoPanelData(String csv) {
    infoPanel.setPortfolioInformationTable(csv);
  }

  public void setCostBasisData(String[] costBasisData) {
    infoPanel.setCostBasisData(costBasisData);
  }

  private void setupInputPanel() {
    inputPanel = new InputPanel();
    inputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainPanel.add(inputPanel);
  }

  public void displayStatusMessage(String message) {
    portfolioPanel.appStatusUpdates.append("\n"+message);
    portfolioPanel.appStatusUpdates.validate();
    JScrollBar vertical = portfolioPanel.scrollStatusPane.getVerticalScrollBar();
    vertical.setValue(vertical.getMaximum());
  }

  private void setupGraphPanel() {
    // TODO Generate stock performance graph here
    graphPanel = new GraphPanel();
    graphPanel.setBackground(Color.lightGray);
    graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainPanel.add(graphPanel);
  }

  public void startUI() {
    JFrameView.setDefaultLookAndFeelDecorated(true);
    setupMainJFrame();
    setupStockListPanel();
    populateStockList();
    setupPortfolioInfoPanel();
    setupInputPanel();
    setupGraphPanel();
    swingUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    swingUI.pack();
    swingUI.setVisible(true);
//    swingUI.setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
//    swingUI.setExtendedState(swingUI.getExtendedState() | swingUI.MAXIMIZED_BOTH);

    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      UIManager.put("OptionPane.minimumSize",new Dimension(480,0));
      UIUtils.centerFrameOnScreen(swingUI);
    } catch (UnsupportedLookAndFeelException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

  }

}
