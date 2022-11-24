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
    String[] portfolios = model.getExistingPortfolios();
    jFrameView = new JFrameView();
    setGraphicalUIFeatures();
    jFrameView.setPortfolioIDLabel(portfolios);
    jFrameView.startUI();
    jFrameView.populateStockList();
  }

  public void setGraphicalUIFeatures() {
    this.jFrameView.setFeatures(this);
  }

  @Override
  public void getPortfolioInformation(String pfID) {
    String status;
    String json = pfID.split("\\.")[0];
    try {
      status = model.getPortfolioValue(new SimpleDateFormat("yyyy-MM-dd").format(
          Calendar.getInstance().getTime()), json);
    }
    catch (ParseException e) {
      status = "Could not read data";
    }
    this.jFrameView.setInfoPanelData(status);
  }
}
