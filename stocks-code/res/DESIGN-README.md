# Design Changes

1. As we were allowed to use external libraries, we used the GSON Parser and converted our
   persistent portfolio type from CSV to JSON which can be handled in a better way via objects.
2. We remodelled the Model to incorporate these CSV->JSON changes, added some more helper methods
   that we would need to read/write a JSON Object/file.
3. We refactored the Controller completely to use Command Design Pattern as the previous code was
   getting bloated with loops. The command design pattern helped us segregate functions into their
   command classes and make the controller light.
4. We implemented ModelView design where the View can directly "pull" data using a model object.
   This helped us reduce commands that we need to include in the controller thereby making the
   controller lighter.
5. When we were using the CSV, we did not map the incoming data to a Java Object, which now we do
   for both PortfolioData and APIData (from Alphavantage). This helped us write elegant methods to
   parse and edit the API Responses and portfolio data. We still keep the functionality where the
   user can provide us a CSV with their existing portfolio and we map it to our JSON Object.
6. Add some more methods to our Model Interface which are used to to show Performance, Edit the
   portfolio, Cost-basis.
7. Added a way to set commission fees for the current app-lifecycle, the user cannot enter a
   negative or 0 value.
8. We just put the Classes into their respective packages to help organize the code.