# Setup Time!

## Run application

The following are the steps to run our Stocks Application! Enjoy :)

1. `cd res` (Move to res folder/open a terminal in res folder)
2. `java -jar STOCKS-PDP-AAKASH-SAMANVYA.jar` (Run the command )

Basically, make sure `app_data` and the `jar` file are in the same folder.

## Load External CSV

You can load up an external CSV in two ways:

1. Select the first option within our application and pass the path to CSV (without the double
   quotes).
2. Copy and paste your CSV into `app_data/PortfolioData` and rename it into a 6-digit number (make
   sure to give a unique number).

## CSV Format

Stock has to be an official and valid ticker symbol, we will ignore invalid symbols (or it'll just
show 0 if no data
is available for it) and we convert (negative quantities) -20 to just 20 assuming you made a typo.
<br>
Just enter the Stock,Quantity in your CSV like:<br>
AAPL,20<br>
MSFT,30

## Detailed Instructions:

### Creating a portfolio with 3 Stocks:

- Once you have run the jar file, you'll see an option to `Create New Portfolio`. Select that by
  entering '3' in the console. Enter your Stock,Quantity in the console like the example provided (
  shows up in console) and after every entry, press 'Enter'. It'll show you the stock you have just
  added. To stop adding stocks to your portfolio simply press 'f' or 'F' in the console.
- You can take these three stocks if you want: AAPL,20;MSFT,30;TSLA,10. The ';' indicates you have
  to press enter. The application will reject more than one stock,quantity entry in one line.
- Once you are done press 'f/F'. Note your portfolio ID (should be printed in the console). It should
  take you back to the starting menu choices.
- Select option '2' now and you should be able to see your Portfolio ID in the list.
- Type in the portfolio ID and hit enter. It should neatly display the stocks and quantity.
- It'll then prompt you to enter a date of format 'YYYY-MM-DD', any weekend or future date is
  considered invalid and you'll be asked to re-enter.
- After entering date (no restrictions per say, stick to the format), you should see the Stock,
  Quantity,Value getting printed.
    - If the Ticker Symbol is not found in our local repository, or with AlphaVantage, you should
      see '
      0' being returned in Value.
    - If Ticker symbol is not found in our local repository, but found on AlphaVantage, it should
      pull
      the data and show the value.
    - If there are more than 5 ticker symbols that are not found in our local repository, you'll
      start
      seeing 'API Limit Reached' from the 6th one.
- Press 'b/B' to go back to the main menu.

### Creating another portfolio with 2 stocks:

- Once you have run the jar file, you'll see an option to `Create New Portfolio`. Select that by
  entering '3' in the console. Enter your Stock,Quantity in the console like the example provided (
  shows up in console) and after every entry, press 'Enter'. It'll show you the stock you have just
  added. To stop adding stocks to your portfolio simply press 'f' or 'F' in the console.
- You can take these three stocks if you want: AMZN,400;JPM,30. The ';' indicates you have
  to press enter. The application will reject more than one stock,quantity entry in one line.
- Once you are done press 'f/F'. Note your portfolio ID (should be printed in the console). It
  should
  take you back to the starting menu choices.
- Select option '2' now and you should be able to see your Portfolio ID in the list.
- Type in the portfolio ID and hit enter. It should neatly display the stocks and quantity.
- It'll then prompt you to enter a date of format 'YYYY-MM-DD', any weekend or future date is
  considered invalid and you'll be asked to re-enter.
- After entering date (no restrictions per say, stick to the format), you should see the Stock,
  Quantity,Value getting printed.
    - If the Ticker Symbol is not found in our local repository, or with AlphaVantage, you should
      see '
      0' being returned in Value.
    - If Ticker symbol is not found in our local repository, but found on AlphaVantage, it should
      pull
      the data and show the value.
    - If there are more than 5 ticker symbols that are not found in our local repository, you'll
      start
      seeing 'API Limit Reached' from the 6th one.
- Press 'b/B' to go back to the main menu.

## List of supported Stocks

We support all stocks, and dates, but if you don't want to run into `API Limit Reached` messages,
consider using the given list of stocks and date equal to or less than "2022-11-01" ie. Nov 1 2022.

- Apple (AAPL)
- Google (GOOG)
- Microsoft (MSFT)
- Tesla (TSLA)
- Microsoft (MSFT)
- GreenPower Motor Company (GPV.TRV)
- Centrica (CNA.LON)
- Amazon (AMZN)
- IBM (IBM)
- JP Morgan (JPM)
- Mastercard (MA)
- UnitedHealth Group (UNH)
- Visa (V)
- Exxon Mobile Corp (XOM)
- Procter & Gamble Co (PG)
- Nike (NKE)
- Pfizer Inc. (PFE)
- Verizon (VZ)
- Walmart (WMT)
- PepsiCo Ltd. (PEP)
- Oracle (ORCL)
- Coco Cola (KO)
- McDonald's Corp (MCD)
- Bank of America (BAC)
- AbbVie Inc (ABBV)
- Accenture (ACN)
- Conoco Philips (COP)
- Cisco (CSCO)
- ProShares (CSM)
- Chevron Corp (CVX)
- Johnson and Johnson (JNJ)
- Netflix (NFLX)
- Nvidia (NVDA)
- Meta (META)
- Texas Instruments Inc. (TXN)
- HomeDepot (HD)