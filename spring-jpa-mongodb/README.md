Spring Data + MongoDB Rest API
===

This is a simple Spring Boot app that shows how to create REST API with MongoDB as persistence storage, and use the highly expressive Groovy + Spock framework for writing integration test.

## Run

First, you'll need to install mongodb, run the following command to start Mongo Server: 

```
$ mongod --dbpath data/db
```    

```bash
$ gradle clean bootRun -PspringProfile=demo
```

## Spring @WebMvcTest with Groovy + Spock Framework
 
```bash
$ gradle clean test
```
