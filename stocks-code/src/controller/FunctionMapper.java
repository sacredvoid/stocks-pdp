package controller;

import java.util.Objects;

/**
 * Helper class to segregate the multiple functionalities and decide what function to execute
 * depending on user inputs
 */

public class FunctionMapper {

  public static boolean selectedYes(String currentInput) {
    return Objects.equals(currentInput, "y") || Objects.equals(currentInput, "Y");
  }
}
