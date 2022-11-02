package model;

/**
 * Helper class to identify the OS that is being used to help out with determining which file
 * separator to use for read/write operations.
 */
public class OSValidator {

  private static String OS = System.getProperty("os.name").toLowerCase();

  /**
   * Helps determine which file separator to use.
   *
   * @return returns the respective file separator for windows (\\) and other OSes(/).
   */
  public static String getOSSeparator() {
    if (isWindows()) {
      return "\\";
    } else {
      return "/";
    }
  }

  private static boolean isWindows() {
    return OS.contains("win");
  }

  private static boolean isMac() {
    return OS.contains("mac");
  }

  private static boolean isUnix() {
    return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
  }

  private static boolean isSolaris() {
    return OS.contains("sunos");
  }

  private static String getOS() {
    if (isWindows()) {
      return "win";
    } else if (isMac()) {
      return "osx";
    } else if (isUnix()) {
      return "uni";
    } else if (isSolaris()) {
      return "sol";
    } else {
      return "err";
    }
  }

}
