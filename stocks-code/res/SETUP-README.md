# Setup Time!

## Run application

The following are the steps to run our Stocks Application! Enjoy :)

1. `cd res` (Move to res folder/open a terminal in res folder)
2. `java -jar *.jar` (Run the command )

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