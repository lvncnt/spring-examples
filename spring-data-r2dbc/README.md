Spring Data R2DBC: Postgres + H2
====
 
### Reference

https://github.com/spring-projects/spring-data-examples/tree/master/r2dbc

### Prerequisites

* PostgreSQL database or H2 embeded database
* Driver for [Reactive Relational Database Connectivity H2 (R2DBC H2)](https://github.com/r2dbc/r2dbc-h2) 

* Driver for [Reactive Relational Database Connectivity H2 (R2DBC PostgreSQL)](https://github.com/r2dbc/r2dbc-postgresql) 

### Run

`gradle clean test` to run `CustomerRepositoryIntegrationTests`. By default, H2 is plugged into the Spring Data R2DBC. It can be switched to Postgres by changing the spring profile to `postgre`. 
 