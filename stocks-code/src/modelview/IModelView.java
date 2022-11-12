package modelview;

import java.io.FileNotFoundException;
import java.text.ParseException;

public interface IModelView {

  String getPortfolio(String portfolioID) throws FileNotFoundException;

  String getPortfolioValue(String date, String portfolioID) throws ParseException;

  String[] getExistingPortfolios();

  String getPortfolioValueByID(String date, String pfID) throws FileNotFoundException;
}
