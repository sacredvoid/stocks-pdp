package model.portfolio;

public class PortfolioDateMapper implements IPortfolioDateMapper {
  private String date;
  private PortfolioData pfData;

  public PortfolioDateMapper(String date, PortfolioData p) {
    this.date = date;
    this.pfData = p;
  }

  @Override
  public String getDate() {
    return this.date;
  }

  @Override
  public PortfolioData getPortfolioObject() {
    return this.pfData;
  }

}
