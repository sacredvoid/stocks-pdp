package model.portfolio;

/**
 * IPortfolioDateMapper interface defines the method signatures required to perform various
 * operations on PortfolioDateMapper Object.
 */
public interface IPortfolioDateMapper {

  /**
   * getDate() method is used to get a date of a record.
   *
   * @return date as a string
   */
  String getDate();

  /**
   * getPortfolioObject() returns the PortfolioDataObject.
   *
   * @return object of PortfolioData
   */
  PortfolioData getPortfolioObject();
}
