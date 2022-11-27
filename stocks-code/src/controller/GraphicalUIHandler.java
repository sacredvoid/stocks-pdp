package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import model.Orchestrator;
import view.gui.JFrameView;

public class GraphicalUIHandler extends AbstractHandler implements GraphicalUIFeatures {

  private Orchestrator model;
  private JFrameView jFrameView;

  public GraphicalUIHandler(Orchestrator model) {
    this.model = model;
  }

  @Override
  public void run() {
    jFrameView = new JFrameView(model);
    jFrameView.startUI();
    jFrameView.populateStockList();
    setGraphicalUIFeatures();
  }

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
    }
    catch (ParseException e) {
      status = "Could not read data";
    }
    this.jFrameView.setInfoPanelData(status);
    this.jFrameView.displayStatusMessage("Showing details for Portfolio ID: "+json+" on date: "+date);
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
    }
    catch (Exception e) {
      status = new String[] {};
    }
    this.jFrameView.setCostBasisData(status);
  }

  @Override
  public void createPortfolio(String pfData) {
    String status;
    if(pfData.equals("")) {
      status = this.model.createPortfolio("no data provided");
    }
    else {
      status = this.model.createPortfolio(pfData);
    }
    this.jFrameView.displayStatusMessage(status);
  }

  @Override
  public void modifyPortfolio(String pfID, String call) {
    String status;
    String json = pfID.split("\\.")[0];
    status = this.model.editExistingPortfolio(json,call);
    if(status.equalsIgnoreCase("sorry")) {
      this.jFrameView.displayStatusMessage(status);
    }
    else {
      String editStatus = "Executed:\n"+ String.join(" ",call.split(","));
      this.jFrameView.displayStatusMessage(editStatus);
    }
  }

  @Override
  public void setCommission(String commission) {
    String status = this.model.setCommissionFees(commission);
    this.jFrameView.displayStatusMessage(status);
  }

}
