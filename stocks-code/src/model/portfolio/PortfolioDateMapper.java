package model.portfolio;

public class PortfolioDateMapper implements PortfolioDateMapperInterface {
  private String date;
  private PortfolioDataData pfData;

  public PortfolioDateMapper(String date, PortfolioDataData p) {
    this.date = date;
    this.pfData = p;
  }

  @Override
  public String getDate() {
    return this.date;
  }

  @Override
  public PortfolioDataData getPortfolioObject() {
    return this.pfData;
  }

}
