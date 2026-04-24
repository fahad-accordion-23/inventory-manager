# Ledge Inventory Manager

![Build Status](https://github.com/fahad-accordion-23/inventory-manager/actions/workflows/maven.yml/badge.svg)

A modular, architectural inventory management system built with Spring Boot and JavaFX.

## Project Structure

- **ledge-contracts**: Shared DTOs and API contracts.

- **ledge-server**: Spring Boot REST API (Command/Query/Domain).

- **ledge-ui**: JavaFX Client application.

## Getting Started

### Prerequisites

- JDK 25
- Maven 3.9+

### Running the Server

```bash
cd ledge-server
mvn spring-boot:run
```

### Running the Client

```bash
cd ledge-ui
mvn javafx:run
```

## Architecture

This project follows a Modular Monolith approach with CQRS and explicit boundary separation between the REST Backend and the JavaFX Frontend.
