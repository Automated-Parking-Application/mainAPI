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


Frontend (Mobile) => Backend (Spring boot) -> HTTP method: GET (get information), POST (create information), PUT, PATCH (update information), DELETE
Frontend -> backend (API Call)
Request get parking attendant , get parking space
Controller  ->  Service (parking attendant service, parking space service)   ->  Repository   (Table from database)  (thao tác với database) save , delete, update, get
return ResponseEntity ()
