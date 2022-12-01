package controller;

import org.jfree.chart.JFreeChart;

public interface GraphicalUIFeatures {

  void getPortfolioInformation(String pfID, String date);

  void getCostBasis(String pfID,String date);

  void createPortfolio(String pfData);

  void modifyPortfolio(String pfID, String call);

  void setCommission(String commission);

  void loadExternalPortfolio(String path);

  JFreeChart getChart(String pfID, String startDate, String endDate);

  void createDCAPortfolio(String dcaData);
}
