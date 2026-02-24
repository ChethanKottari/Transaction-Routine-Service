#!/bin/bash

echo "Starting Transaction Routine Service..."

docker compose up --build -d

echo "Application is up!"
echo "http://localhost:8085/actuator/health"