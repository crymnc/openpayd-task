# Exchange Rate API

## Exchange Rate API made for Openpayd

### Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* Spring Boot
* Java 11

### Task Description

Developing a simple foreign exchange application which has capabilities like returning exchange rates, converting given amount to another currency
and saving and listing transactions which was done before in api.

### Specification
1. Exchange Rate API
   * input: currency pair to retrieve the exchange rate
   * output: exchange rate
2. Conversion API:
   * input: source amount, source currency, target currency
   * output: amount in target currency, and transaction id.
3. Conversion List API
   * input: transaction id or transaction date (at least one of
   the inputs shall be provided for each call)
   * output: list of conversions filtered by the inputs and paging is
   required
4. The application shall use a service provider to retrieve
   exchange rates and optionally for calculating amounts
5. In the case of an error, a specific code to the error and a
   meaningful message shall be provided.

### Documentation

* [Swagger](http://localhost:8200/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/) - Swagger Documentation
