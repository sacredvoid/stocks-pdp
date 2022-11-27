package controller;

public interface GraphicalUIFeatures {

  void getPortfolioInformation(String pfID, String date);

  void getCostBasis(String pfID,String date);

  void createPortfolio(String pfData);

  void modifyPortfolio(String pfID, String call);

  void setCommission(String commission);
}
