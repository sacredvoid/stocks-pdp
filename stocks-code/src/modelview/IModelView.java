package modelview;

import java.io.FileNotFoundException;
import java.text.ParseException;

public interface IModelView {

  String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException;

  String getPortfolioValue(String date, String portfolioID) throws ParseException;

  String[] getExistingPortfolios();

  String getPortfolioCompositionByDate(String date, String pfID) throws FileNotFoundException;

  String[] getCostBasis(String pfID, String date);
}
