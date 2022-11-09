package model.portfolio;

public class PortfolioDateMapper {
  private String date;
  private PortfolioData pfData;

  public PortfolioDateMapper(String date, PortfolioData p) {
    this.date = date;
    this.pfData = p;
  }

  public String getDate() {
    return this.date;
  }

  public PortfolioData getPortfolioObject() {
    return this.pfData;
  }

}
