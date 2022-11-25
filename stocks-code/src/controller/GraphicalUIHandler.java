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
      status = model.getCostBasis(pfID, date);
    }
    catch (Exception e) {
      status = new String[] {};
    }
    this.jFrameView.setCostBasisData(status);
  }

}
