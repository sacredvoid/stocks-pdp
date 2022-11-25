package view;

/**
 * Our view interface which handles the outputs from our application (model) and defines methods
 * that need to be implemented to view the outputs.
 */
public interface ViewHandler {

  /**
   * Gets the given PF and date's cost basis calculation.
   * @param pfID string pfid
   * @param date string date
   */
  void printCostBasis(String pfID, String date);

  /**
   * Method that takes in a string and color and attaches it to a PrintStream object.
   *
   * @param text  to print/attach to PrintStream object
   * @param color the first letter of a color, currently supports only "R"ed,"G"reen,"Y"ellow
   */
  void printText(String text, String color);

  /**
   * Method that takes in data and prints it in a tabular form for easy understanding.
   *
   * @param data portfolio data as string
   */
  void printTabularData(String data);
}
