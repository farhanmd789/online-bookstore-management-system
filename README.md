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

## Sreenshots
<img width="1920" height="1020" alt="Screenshot 2026-03-13 144755" src="https://github.com/user-attachments/assets/ccc0376c-bc19-4f72-a90c-f3150aa551ca" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 144948" src="https://github.com/user-attachments/assets/3c634058-fbba-401c-83a5-f94083d2d750" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145106" src="https://github.com/user-attachments/assets/2bdbb11f-c436-41c6-ab65-d4c774c343e5" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145156" src="https://github.com/user-attachments/assets/6478e4cd-e903-407e-9efe-8b18d257bb27" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145315" src="https://github.com/user-attachments/assets/7f1e56a9-c632-448d-92ae-1a5486ab998e" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145406" src="https://github.com/user-attachments/assets/97b30a02-b4b4-47a4-af1f-30bff274de8e" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145636" src="https://github.com/user-attachments/assets/36b72e2a-2f05-429a-87ec-e06c551e4bf0" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145806" src="https://github.com/user-attachments/assets/78b21dbe-dbef-4903-9fe3-b5c53b5f8713" />

## Admin panel
<img width="1920" height="1020" alt="Screenshot 2026-03-13 145913" src="https://github.com/user-attachments/assets/5c0b473c-ec9e-47e5-9054-a8406207917d" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 150013" src="https://github.com/user-attachments/assets/e5dfbbbd-7b0d-4113-92a2-0d2860fdf240" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 150052" src="https://github.com/user-attachments/assets/12753080-a936-4733-89aa-753698c7ef2a" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 150258" src="https://github.com/user-attachments/assets/113d796b-9add-4cb2-a762-1cbff72a05d6" />
<img width="1920" height="1020" alt="Screenshot 2026-03-13 150412" src="https://github.com/user-attachments/assets/fe8febfd-999a-46fb-ab97-889927115e2c" />



## Contribution

See `CONTRIBUTING.md` for contribution guidance.

## License

No license file is currently included in this repository.

