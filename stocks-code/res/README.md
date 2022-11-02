# stocks-pdp

Assignment: Stocks-1

## Our Program Features

1. **Creation of Portfolio**

- [x] User Input (Stocks,Quantity) into console
- [x] User Input (path to external CSV) into console

2. **View existing Portfolios**

- [x] User input (portfolio ID) into console
- [x] No option to update existing portfolios

3. **Determine Total Portfolio Value**

- [x] User input (date) into console
- [x] Handle weekends (asks to enter date again)
- [x] Return NA if no value available for given stock and date

4. **Portfolio persistance**

- [x] Portfolio data saved as CSV file in `./app_data/PortfolioData`

5. **Extras**

- [x] Data Validation (Stock Scrip checked with a regex, quantity rounded off to nearest int)

## Limitations

1. Our application supports any stock realistically, but the user will see an `API Limit Reached`
   message in the Portfolio Table for any stock not in our 30 stock list and also if the user
   wants latest data, we have a feature that fetches latest data from the API after 5pm, so that
   is also when you'll see an `API Limit Reached`. Retrying every min, it should go away and show
   the value.
2. No other limitation, but we have a really strict data validator that will check for valid stock
   symbols and rounds off any non-integer quantity, also ignores rows with missing data (Like GOOG,)
   .
