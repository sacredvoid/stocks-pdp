# Prerequisite

## External Libraries (included with the JAR)

- Gson Library for JSON Parsing and reading.
  License: https://github.com/google/gson/blob/master/LICENSE
  
- SwingX Core Library for JXDatePicker. Used to show the calendar date picker.
  License: https://github.com/cuba-platform/swingx-core/blob/master/src/main/java/org/jdesktop/swingx/JXDatePicker.java

- JFreeChart Library for drawing charts.
  License: https://www.jfree.org/lgpl.php

The libaries are packaged with our jar.

## Running our Application via JAR

- Open a terminal and `cd` into the `res` folder from our project.
- Run our JAR file with the command: `java -jar STOCKS3-PDP-AAKASH-SAMANVYA.jar`
- Follow the prompts to pick either Text Based (with lesser features) or Graphical UI (with DCA Strategy feature).

## What to Expect
- You will have to start by creating a new portfolio. It's as simple as clicking the button on the bottom right panel "Create Portfolio" and it will ask you to enter stocks, quantity, date that you want to execute the purchase on. Once it's done and you click 'OK' you'll see the stock list (on the first panel) update and you can go ahead and select it (note: you have to select it from drop-down) and pick the date and then click on "Show Portfolio". This should display the Portfolio Value and Cost-Basis of your portfolio till that date.
- All application status updates are visible on the panel and you can refer it to see the actions executed.
- There's a button to change the cost-basis in the buttons panel (bottom right).
- You can also get graphs for a given date range in the bottom right panel. 
- For the best experience, please maximize the Application Window!
- You can create a new portfolio with DCA strategy or use an existing (normal/dca) portfolio and add/update the strategy that's currently applied. You can also view the existing applied strategy using the "View Current Strategies" in the bottom left panel.
- You can also load an existing portfolio into the application (CSV File with format: Stock,Quantity,Date(yyyy-MM-dd)\nStock,Quantity,Date(yyyy-MM-dd)... and so on. 


## Stock Support
We support all the stocks that are supported by AlphaVantage API. If there's no data/market was closed on that day, we get the most recent stock data for that date if we are showing you the PF Value, and if we are trying to buy and it's a holiday, we get the next available Stock Data from the API.
