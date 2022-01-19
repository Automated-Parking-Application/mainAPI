Prerequisite of Spring Boot:
  JDK
  Maven Apache
  Maven Wrapper

Database:
  MySQL (with name of database: apa)
  Running init.sql in the first time create database

Update database config at application.properties with username and password of database:
  spring.datasource.username=root
  spring.datasource.password=IamLeo0708

Tools:
  MySQL Workbench or TablePlus

init maven wrapper:
  mvn -N io.takari:maven:wrapper

Install:
  ./mvnw clean install

Run:
  ./mvnw spring-boot:run

Service running at localhost:8080
