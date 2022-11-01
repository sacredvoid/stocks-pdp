package model;

import java.io.FileNotFoundException;
import java.text.ParseException;

public interface Orchestrator {

    String getPortfolio(String portfolioID) throws FileNotFoundException;
    String createPortfolio(String portfolioData);
    String generatePortfolioID();
    String getPortfolioValue(String date, String data) throws ParseException;
    String[] showExistingPortfolios();


}
