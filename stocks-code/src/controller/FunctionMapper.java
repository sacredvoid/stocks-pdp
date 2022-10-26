package controller;

import java.util.Objects;

/**
 * Helper class to segregate the multiple functionalities and decide what function to execute
 * depending on user inputs
 */

public class FunctionMapper {

  String currentInput;

  public FunctionMapper() {
    this.currentInput = "";
  }

  public FunctionMapper(String s) {
    this.currentInput = s;
  }

  public boolean decider() {
    return Objects.equals(currentInput, "y") || Objects.equals(currentInput, "Y");
  }
}
