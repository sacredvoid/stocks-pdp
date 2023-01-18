package controller;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import model.Orchestrator;
import org.jfree.chart.JFreeChart;
import view.gui.JFrameView;

/**
 * The implementation of the GraphicalUIFeatures which acts as the GUI Controller class. This takes
 * care of all the features and exchanges data between view-model and acts on the user interaction.
 */
public class GraphicalUIHandler extends AbstractHandler implements GraphicalUIFeatures {

  private Orchestrator model;
  private JFrameView jFrameView;

  /**
   * Instantiates a new Graphical ui handler.
   *
   * @param model the model
   */
  public GraphicalUIHandler(Orchestrator model) {
    this.model = model;
  }

  @Override
  public void run() {
    // initializing ViewModel
    jFrameView = new JFrameView(model);
    jFrameView.startUI();
    setGraphicalUIFeatures();
  }

  /**
   * Sends the features to the JFrameView which then sets the features to the respective button
   * action listeners.
   */
  public void setGraphicalUIFeatures() {
    this.jFrameView.setFeatures(this);
  }

  @Override
  public void getPortfolioInformation(String pfID, String date) {
    if (date.isEmpty()) {
      date = new SimpleDateFormat("yyyy-MM-dd").format(
          Calendar.getInstance().getTime());
    }
    String status;
    String json = pfID.split("\\.")[0];
    try {
      status = model.getPortfolioValue(date, json);
    } catch (ParseException e) {
      status = "Could not read data";
    }
    this.jFrameView.setInfoPanelData(status);
    this.jFrameView.displayStatusMessage(
        "Showing details for Portfolio ID: " + json + " on date: " + date);
  }

  @Override
  public void getCostBasis(String pfID, String date) {
    if (date.isEmpty()) {
      date = new SimpleDateFormat("yyyy-MM-dd").format(
          Calendar.getInstance().getTime());
    }
    String[] status;
    String json = pfID.split("\\.")[0];
    try {
      status = model.getCostBasis(json, date);
    } catch (Exception e) {
      status = new String[]{};
    }
    this.jFrameView.setCostBasisData(status);
  }

  @Override
  public void createPortfolio(String pfData) {
    String status;
    status = this.model.createPortfolio(pfData);
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void modifyPortfolio(String pfID, String call) {
    String status;
    String json = pfID.split("\\.")[0];
    status = this.model.editExistingPortfolio(json, call);
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void setCommission(String commission) {
    if (commission.isEmpty()) {
      this.jFrameView.displayStatusMessage("No commission entered, using default $1!");
      return;
    }
    String status = this.model.setCommissionFees(commission);
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void loadExternalPortfolio(String path) {
    if (path.isEmpty()) {
      this.jFrameView.displayStatusMessage("No file selected!");
      return;
    }
    String status = "";
    try {
      status = this.model.loadExternalPortfolio(path);
    } catch (FileNotFoundException e) {
      this.jFrameView.displayStatusMessage("Load: File not found!");
    }
    this.jFrameView.displayStatusMessage("Load:" + status);
  }

  @Override
  public JFreeChart getChart(String pfID, String startDate, String endDate) {
    String json = pfID.split("\\.")[0];
    JFreeChart newChart = this.model.generateTimeSeriesData(json, startDate, endDate);
    if (newChart == null) {
      this.jFrameView.displayStatusMessage("Graph: " + this.model.getBuildGUIGraphStatus());
      return null;
    } else {
      return newChart;
    }
  }

  @Override
  public void createDCAPortfolio(String dcaData) {
    String status = this.model.createDCAPortfolio(dcaData);
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void addDCAToExistingPF(String pfID, String dcaData) {
    String json = pfID.split("\\.")[0];
    String status = this.model.existingPortfolioToDCAPortfolio(json, dcaData);
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void viewAppliedDCA(String pfID) {
    String json = pfID.split("\\.")[0];
    String status;
    try {
      status = this.model.getPredefinedStrategies(json);
    } catch (FileNotFoundException e) {
      status = "File not found!";
    }
    this.jFrameView.displayInfoPopUp(status);
  }

}
