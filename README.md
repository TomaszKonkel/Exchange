# Web Service which fetch data from ECB (European Central Bank)

After collecting data user can send a request with basic parameters (Currency From, Currency To, Date) and response will be a rate for found exchange

Some of exchange rate can't be found because currency doesn't exist for specific dates. For this situation user will be informed by text information with description.
There is one main method which searching for rate. If returned value is null, second method look for cause of such result.

Data is save in database if table is empty. Every day at 4 p.m will be executed method which check the newest date (from ECB and repo). If both date don't match, then to table will be added records with new courses. Scheduling is based on Java Cron Expression. 

Whole application work with Spring Boot and MVC design pattern. Api was made in accordance with REST Api rules.


