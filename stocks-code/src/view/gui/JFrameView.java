package view.gui;

import controller.GraphicalUIFeatures;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.swingx.JXDatePicker;

public class JFrameView extends JFrame implements ActionListener {

  private JPanel mainPanel;
  private PortfolioListPanel portfolioPanel;
  private String[] portfolioList = new String[]{};
  private JPanel graphPanel;
  private JPanel inputPanel;
  private JPanel tablePanel;
  private JPanel datePickerPanel;
  private JScrollPane mainScrollPane;
  private JTable portfolioData;
  private InfoPanel infoPanel;
  private GraphicalUIFeatures features;
  private JXDatePicker datePicker;

  private JFrameView swingUI;

  public JFrameView() {
    super();
    this.swingUI = this;
  }

  private void setupMainJFrame() {
    setTitle("Aakasam Stock Trading");
    setSize(750,750);
    //TODO Test different layouts
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(0,2));
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);
  }

  // TODO Add features to map commands

  private void setupStockListPanel() {
    //TODO read json files and list them here
    portfolioPanel = new PortfolioListPanel();
    mainPanel.add(portfolioPanel);
  }

  public void populateStockList() {
    portfolioPanel.setupPortfolioList(this.portfolioList,this.features);
  }

  public void setPortfolioIDLabel(String[] portfolios) {
    this.portfolioList = portfolios;
  }

  public void setFeatures(GraphicalUIFeatures features) {
    this.features = features;
  }

  private void setupPortfolioInfoPanel() {
    infoPanel = new InfoPanel();
    mainPanel.add(infoPanel);
    // Create date and table panel
  }

  public void setInfoPanelData(String csv) {
    infoPanel.setPortfolioInformationTable(csv);
  }

  private void setupInputPanel() {
    //TODO Create buttons here for new portfolio
    //TODO Create input flow for buying/selling and sending info to controller
    inputPanel = new JPanel();
    inputPanel.setBackground(Color.GRAY);
    mainPanel.add(inputPanel);
  }

  private void setupGraphPanel() {
    // TODO Generate stock performance graph here
    graphPanel = new JPanel();
    graphPanel.setBackground(Color.lightGray);
    mainPanel.add(graphPanel);
  }


  @Override
  public void actionPerformed(ActionEvent e) {

  }

  public void startUI() {
    JFrameView.setDefaultLookAndFeelDecorated(false);
    setupMainJFrame();
    setupStockListPanel();
    setupPortfolioInfoPanel();
    setupInputPanel();
    swingUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    swingUI.pack();
    swingUI.setVisible(true);

    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
