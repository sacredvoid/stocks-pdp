# Design Document

This document explains our design choices, classes, methods and internal working of our
application

## Portfolio File Type

We chose CSV for this assignment due to the following reasons:

- Easier for user to make (can make it in excel for example)
- Easier for us to parse (since there was a restriction on using external JSON/XML parsers)

## Model-View-Controller (MVC) Design Pattern

This assignment required us to implement the MVC design pattern so that is what we did! We have
our `ClientRunner` file which gives a demo of how other clients can integrate our backend into
their applications. `ClientRunner` initializes our `InteractionHandler` (aka Controller) with an
`InputStream` and `OutputStream`. The `ClientRunner` then relinquishes control to our
`InteractionHandler` and thus starts the actual logical flow of the program.
`UserInteraction` talks to `InteractionHandler` which talks to `ModelOrchestrator`
. `InteractionHandler` delegates the tasks to `UserInteraction` and `ModelOrchestrator`.

### Interaction Handler (aka Controller)

This is an implementation of the `Handler` Interface and has a main `run()` method which contains
the main logic of our Stock Platform. It also contains additional `getInput()` and `validateInput()`
methods which as the name suggests, get and validate inputs from the user (console). The main
workflow of our program is written in the `run()` method and here we can see that it allows the
user to:

- **Load External Portfolio**:
    1. Asks the user to enter the path to their Portfolio. It has to be a CSV file.
    2. Checks if file exists and if it's a CSV, if yes, then firstly reads the contents, validates
       it
       for ingestion and creates a new portfolio file under the `./app_data/PortfolioData`
       directory,
       our central repository for all Portfolios created in our application.
- **View Exisiting Portfolio**:
    1. Loads up portfolios from `./app_data/PortfolioData` if any found, and asks the user to enter
       their 6-digit portfolio number. Again, there is validation for filename check, it has to be a
       6-digit number.
    2. Displays the Stocks,Quantities from the Portfolio File.
    3. Additional option to view Portfolio value on certain date.
- **Create New Portfolio**:
    1. Ask the user to enter Stock Name, Quantity into the console (there is data validation again),
       one entry per line.
    2. Press `f/F` to finish entering the stock, which then shows the user the 6-digit portfolio
       number that was generated which then shows up when they view existing portfolios.
- **Quit the application with `q/Q`**

### UserInteraction (aka View)

This is an implementation of the `ViewHandler` interface which has two main methods namely:
`printText()` and `printPortfolioData()` which as the name suggest correctly, append the output
coming from the `model` to our `view` for the user to see and act upon. For better readability and
UI, we added colors to our `printText()` method as a parameter. This added slight complexity to our
test-cases but not something we can't handle.

### ModelOrchestrator (aka Model)

This is the backbone of the application and contains all the functionality that our application
has to offer, like reading the Portfolio Data, fetching Stock Data for a given date and showing the
total Portfolio Value. It implements `Orchestrator` interface and implements the methods as
described above.

- **CSVFileOps** : Class that contains our basic read/write operations and data validation.
- **OSValidator** : Class that helps determine which OS and thus which file separator to use.
- **PortfolioValue** : Calculates the total portfolio value given price and stock list.
- **RequestHandler** : Creates the AlphaVantage request URL, calls the API and returns response.
- **StockHandler** : Stores the response from AlphaVantage API, parses the data given a date and
  helps make our API hits more efficient.

### Static Stock Data

We are using the AlphaVantage API which has a limit of 5 hits per min and 500 per day. Since we did
not want to give our user a bad experience, we statically stored 30 most popular stocks that is
critical to our application and UX. This helps us support upto 30 stocks at any instant, the user
is free to add whichever stock they want, if the API limit is reached, we will show
it on the table like `API Limit Reached`, and if the user tries again later, they can access any
stock supported by AlphaVantage. <br>
We also have a smart feature where we fetch the latest stock data (and store it in our Static Stock
Data Repo) from AlphaVantage after 6pm (usually the time when it is available for current day given
it's not weekend). This does run into API Hit but we have an idea to get around it for the next
part of the assignment.

### Data Validators

1. **Input Validation**: This is done in the `InteractionHandler` using
   the `validateInput(String regex)` method which accepts a regex and matches user input with it. We
   went ahead with regex as it gives us more control and flexibility on string checks. We have many
   checks like valid stock ticker symbol, CSV style input (Stock,Quantity) and other flow inputs
   like 'q/Q' to exit.
2. **CSV Validation**: Again, using regex and some basic logic to ignore missing data and rounding
   off quantities to nearest integers.
3. **Date Validation**: If the date is a weekend or future, we ignore it and tell the user to enter
   again.