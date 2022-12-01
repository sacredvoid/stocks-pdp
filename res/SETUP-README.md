# Prerequisite

## External Libraries (included with the JAR)

- Gson Library for JSON Parsing and reading.
  License: https://github.com/google/gson/blob/master/LICENSE

The Gson library is packaged with our jar.

## Running our Application via JAR

- Open a terminal and `cd` into the `res` folder from our project.
- Run our JAR file with the command: `java -jar STOCKS2-PDP-AAKASH-SAMANVYA.jar`
- Follow the prompts to create/update portfolios and view performance

## Creating Portfolio with 3 stocks

- Enter "5" after running the application and hit enter. This should prompt you to enter the
  STOCK,QUANTITY,DATE into the command prompt.
- Enter:
  - "AAPL,20,2022-10-10" then hit enter, followed by 
  - "TSLA,10,2022-11-02" and hit enter followed
    by 
  - "GOOG,15,2021-07-15" and then
  - "q" and hit enter.
- This would create the portfolio and display the portfolio ID.
- Now enter "4" to see the Portfolio Value, then enter the portfolio ID which you just got for your portfolio.
- Then hit enter, and you'll be prompted to enter a date. Enter lets say "2022-11-17" and then hit enter.
- This will then display the portfolio value in a table with:
  - AAPL: 3014.40
  - TSLA: 1831.70
  - GOOG:1477.50
  - Total: 6323.60.
- Now enter "7" to see the Cost-basis, and then when prompted enter your portfolio ID, followed by a date, let's say "2022-11-17", then hit enter.
- You'll see that it prints the cost-basis like: 
  - Total Amount Invested: $44338.15
  - Total Commission Charged: $3.0
  - Total Amount+Commission: $44341.15
  - Total Earned by Selling: $0.0
- Now to Portfolio Value and Cost-basis for another date, repeat by entering "4" option and entering your portfolio ID.
- This time try with a different date let's say "2021-08-15", you'll see:
  - GOOG:1477.50
- There was no other buy transaction before this date.
- Now onto Cost-basis, enter "7" and then when prompted enter the same date "2021-08-15", you'll see:
  - Total Amount Invested: $39379.95
  - Total Commission Charged: $1.0
  - Total Amount+Commission: $39380.95
  - Total Earned by Selling: $0.0
- As expected, commission fees is $1 since only GOOG transaction took place before the entered date.


## Stock Support
We support all the stocks that are supported by AlphaVantage API. If there's no data/market was closed on that day, we return "0" value for that stock.
