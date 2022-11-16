package controller;

public class ValidateData {
    public static String getRegex(String type) {
      switch (type) {
        case "stock":
          return "([A-Z])+";
        case "portfolio":
          return "[0-9]{6}";
        case "quantity":
          return "[0-9]+";
        case "date":
          return "(19|20)[0-9]{2}-[0-9]{2}-[0-9]{2}";
        case "call":
          return "BUY|SELL";
        case "quit":
          return "q|Q";
        default:
          return "";
      }
    }

    public static String getComplexRegex(String[] args) {
      StringBuilder s = new StringBuilder();
      for (String regex: args
      ) {
        s.append(ValidateData.getRegex(regex));
        if(regex != args[args.length - 1]) {
          s.append(",");
        }
      }
      return s.toString();
    }
}
