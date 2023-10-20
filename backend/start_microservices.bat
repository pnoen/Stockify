@echo off
REM This script will run all the Spring Boot microservices with Gradle and log the output.

REM Navigate to each service directory, start the Spring Boot application, and log the output.

cd business-link-service
start cmd /k gradlew bootRun > business-link-service.log
cd ..

cd order-management
start cmd /k gradlew bootRun > order-management.log
cd ..

cd product-service
start cmd /k gradlew bootRun > product-service.log
cd ..

cd user-account-service
start cmd /k gradlew bootRun > user-account-service.log
cd ..

cd user-management-service
start cmd /k gradlew bootRun > user-management-service.log
cd ..

echo All microservices are starting and logging...
pause
