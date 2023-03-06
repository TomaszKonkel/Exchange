# Web Service which fetch data from ECB

After collecting data user can send a request with basic parameters (Currency From, Currency To, Date) and response will be a rate for found exchange

Some of exchange rate can't be found because currency doesn't exist for specific dates. For this situation user will be informed by text information with description.
There is one main method which searching for rate. If returned value is null, second method look for cause of such result.


At this moment data is saved in database if table is empty. Still should be a function which control that date from database is different from ECB (Comparing last date and if not equal add courses)
This will be working in specific amount of time (For example, checking will be executed everyday at 5 p.m, not including weekends). 
