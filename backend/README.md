# Reward Services
# Project Overview
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.   A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).   Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.

This Project is developed using Spring boot and using RESTful Microservices Architecture.

## Tech used

* Spring Boot
* REST microservices
* Spring JPA
* Spring-HATEOAS
* Spring-Actuator

## Testing

* Junit with Mockito

## Steps to run

* Install Git (https://git-scm.com/downloads)
* Make sure you have Java EE installed 
* Clone repository using below command
  `git clone https://github.com/
* Navigate to root directory of the project
* Right Click on the root directory and Run on Server(Apache)
* You must be able to hit the enpoints with postman at
          https://localhost:8080/api/v1/customers
          https://localhost:8080/api/v1/orders
          https://localhost:8080/api/v1/rewards

