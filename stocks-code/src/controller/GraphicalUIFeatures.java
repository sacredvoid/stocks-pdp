package controller;

import org.jfree.chart.JFreeChart;

/**
 * The interface Graphical ui features which defines all methods that are implemented in our GUI and
 * these features are passed to the JFrameView to act on actions from buttons.
 */
public interface GraphicalUIFeatures {

  /**
   * Gets portfolio information.
   *
   * @param pfID the pf id
   * @param date the date
   */
  void getPortfolioInformation(String pfID, String date);

  /**
   * Gets cost basis.
   *
   * @param pfID the pf id
   * @param date the date
   */
  void getCostBasis(String pfID, String date);

  /**
   * Creates portfolio.
   *
   * @param pfData the pf data
   */
  void createPortfolio(String pfData);

  /**
   * Modify portfolio.
   *
   * @param pfID the pf id
   * @param call the call
   */
  void modifyPortfolio(String pfID, String call);

  /**
   * Sets commission.
   *
   * @param commission the commission
   */
  void setCommission(String commission);

  /**
   * Load external portfolio (CSV) into the application.
   *
   * @param path the path
   */
  void loadExternalPortfolio(String path);

  /**
   * Gets the JFreeChart required by our ChartPanel to display the updated portfolio performance.
   *
   * @param pfID      the pf id
   * @param startDate the start date
   * @param endDate   the end date
   * @return the chart
   */
  JFreeChart getChart(String pfID, String startDate, String endDate);

  /**
   * Creates a dollar-cost averaging portfolio. Takes CSV input of the data from the UI like
   * recurring cycle, amount, start date, end date and stock:weights.
   *
   * @param dcaData the dca data
   */
  void createDCAPortfolio(String dcaData);

  /**
   * Add dca to existing normal portfolio.
   *
   * @param pfID    the pf id
   * @param dcaData the dca data
   */
  void addDCAToExistingPF(String pfID, String dcaData);

  /**
   * View applied strategy (DCA in this case).
   *
   * @param pfID the pf id
   */
  void viewAppliedDCA(String pfID);
}
