package modelview;

import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 * The interface Model view.
 */
public interface IModelView {

  /**
   * Gets latest portfolio composition.
   *
   * @param portfolioID the portfolio id
   * @return the latest portfolio composition
   * @throws FileNotFoundException the file not found exception
   */
  String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException;

  /**
   * Gets portfolio value.
   *
   * @param date        the date
   * @param portfolioID the portfolio id
   * @return the portfolio value
   * @throws ParseException the parse exception
   */
  String getPortfolioValue(String date, String portfolioID) throws ParseException;

  /**
   * Get existing portfolios string [ ].
   *
   * @return the string [ ]
   */
  String[] getExistingPortfolios();

  /**
   * Gets portfolio composition by date.
   *
   * @param date the date
   * @param pfID the pf id
   * @return the portfolio composition by date
   * @throws FileNotFoundException the file not found exception
   */
  String getPortfolioCompositionByDate(String date, String pfID) throws FileNotFoundException;

  /**
   * Get cost basis string [ ].
   *
   * @param pfID the pf id
   * @param date the date
   * @return the string [ ]
   */
  String[] getCostBasis(String pfID, String date);
}
