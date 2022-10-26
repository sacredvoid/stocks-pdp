public class PrintHeader {

  public static void printHeader() {
    printLogo();
    printCompanyTitle();
  }
  public static void printCompanyTitle() {
    System.out.println("Welcome To Aaka-Sam Stock Trading!");
    System.out.println("You can always quit the platform by pressing 'q'");
  }
  public static void printLogo() {
    int n = 4, i, j, space = 1;
    space = n - 1;
    for (j = 1; j<= n; j++)
    {
      for (i = 1; i<= space; i++)
      {
        System.out.print(" ");
      }
      space--;
      for (i = 1; i <= 2 * j - 1; i++)
      {
        System.out.print("*");
      }
      System.out.println("");
    }
    space = 1;
    for (j = 1; j<= n - 1; j++)
    {
      for (i = 1; i<= space; i++)
      {
        System.out.print(" ");
      }
      space++;
      for (i = 1; i<= 2 * (n - j) - 1; i++)
      {
        System.out.print("*");
      }
      System.out.println();
    }
  }


}
