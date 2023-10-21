#!/bin/bash

# Declare a list of all service directories
declare -a services=(
  "business-link-service"
  "order-management"
  "product-service"
  "user-account-service"
  "user-management-service"
)

# Function for starting the services
start_service() {
  local service_dir="$1"

  # Navigate to the service directory
  cd "$service_dir" || return

  # Start the service with Gradle, running in the background, and log output
  ./gradlew bootRun > "$service_dir.log" 2>&1 &

  # Navigate back to the original directory
  cd - >/dev/null

  echo "Service $service_dir is starting..."
}

# Iterate through the service directories and start them
for service in "${services[@]}"; do
  start_service "$service"
done

echo "All microservices are starting... logs are being generated in their respective directories."