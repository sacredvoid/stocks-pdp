# stocks-pdp

Assignment: Stocks-2

## Our Program Features

1. **Creation of Portfolio**

- [x] User Input (Stocks,Quantity, Date) into console
- [x] User Input (path to external CSV) into console

2. **View existing Portfolios**

- [x] User input (portfolio ID) into console
- [x] No option to update existing portfolios

3. **Determine Total Portfolio Value**

- [x] User input (date) into console
- [x] Handle weekends (asks to enter date again)
- [x] Return NA if no value available for given stock and date

4. **Portfolio persistance**

- [x] Portfolio data saved as JSON file in `./app_data/PortfolioData`

5. **Portfolio Updation**

- [x] Update the given portfolio depending on user operation ie. BUY/SELL
- [x] Does not update the transaction if executed on a weekend/future
- [x] Also handles if there's no entry of a stock, won't be able to sell.

6. **Stock Data API Modification**

- [x] Add support for integrating other APIs that are there example Yahoo through well designed
  stock data interfaces

7. **Cost Basis + Commission**
- [x] Calculates the stock data cost basis (total invested + commission)
- [x] Commission can be entered by the user and cannot be negative or zero

8. **Performance of Portfolio**
- [x] The performance graph needs a minimum of 5 days, and can render maximum upto 30 years graph

9. **Extras**

- [x] Data Validation (Stock Scrip checked with a regex, quantity rounded off to nearest int)