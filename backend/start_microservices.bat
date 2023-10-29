@echo off
REM This script will run all the Spring Boot microservices with Gradle and log the output.

REM Navigate to each service directory, start the Spring Boot application, and log the output.

cd business-link-service
start cmd /k gradlew bootRun
cd ..

cd order-management
start cmd /k gradlew bootRun 
cd ..

cd product-service
start cmd /k gradlew bootRun
cd ..

cd user-account-service
start cmd /k gradlew bootRun
cd ..

cd user-management-service
start cmd /k gradlew bootRun 
cd ..

echo All microservices are starting and logging...
pause
