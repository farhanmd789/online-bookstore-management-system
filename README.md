# Bookverse - Online Book Store

Bookverse is a Spring Boot web application for managing an online book store.
It provides separate flows for sellers (admin) and customers, with features such as inventory management, browsing, cart, checkout, and order history.

## Tech Stack

- Java 8
- Spring Boot 2.7.18
- Spring MVC + Thymeleaf
- Maven
- MySQL

## Features

### Seller (Admin)
- Seller login
- View all books in store
- Add new books
- Update existing books
- Remove books

### Customer
- Register and login
- Browse all books
- Search books
- Add/remove cart items
- Checkout and payment flow
- View order history

## Application Routes

### Public
- `GET /` -> Home page
- `GET /login` -> Role selection/login page

### Seller
- `GET /adminlog`, `POST /adminlog`
- `GET /storebooks`
- `GET /addbook`, `POST /addbook`
- `GET /updatebook`, `POST /updatebook`
- `GET /removebook`, `POST /removebook`

### Customer
- `GET /userlog`, `POST /userlog`
- `GET /userreg`, `POST /userreg`
- `GET /viewbook`, `POST /viewbook`
- `GET /searchbook`
- `GET /cart`, `POST /cart`
- `POST /checkout`
- `POST /pay`
- `GET /orders`
- `GET /logout`

## Project Structure

Key folders:

- `src/main/java/com/farhancode` - Java source files
- `src/main/resources/templates` - Thymeleaf HTML templates
- `src/main/resources/application.properties` - app configuration
- `setup/CreateDatastore.sql` - database setup script
- `scripts` - deployment/start-stop helper scripts

Note: Package names in source files are currently under `com.bittercode`.

## Prerequisites

- Java 8 installed and configured in `JAVA_HOME`
- Maven 3.x
- MySQL server running on localhost

## Database Setup

1. Create the database and tables using:
	 - `setup/CreateDatastore.sql`
2. Ensure your MySQL credentials match:
	 - `src/main/resources/application.properties`

Default configuration in `application.properties`:

- Database URL: `jdbc:mysql://localhost:3306/onlinebookstore`
- Username: `root`
- Password: `root`
- Server port: `9090`

Important:
- The script contains `\c onlinebookstore`, which is PostgreSQL-style syntax.
- If you run the script in MySQL, remove that line and run the remaining statements.

## Run Locally

From the project root:

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```


Open:

- `http://localhost:9090/`

## Build Task (VS Code)

This workspace includes a task:

- Build and Run Online Book Store

It executes:

```bash
mvn clean package -DskipTests
```

## Troubleshooting

- Port issue:
	- Change `server.port` in `src/main/resources/application.properties`.
- Database connection issue:
	- Verify MySQL is running and credentials are correct.
- Build failures:
	- Run `mvn -v` and confirm Java 8 is being used.

## Contribution

See `CONTRIBUTING.md` for contribution guidance.

## License

No license file is currently included in this repository.
