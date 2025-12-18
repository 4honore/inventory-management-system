# Inventory Management System (Java Swing)

A robust desktop application for managing inventory, built with Java Swing. This system helps businesses track products, manage stock levels, handle orders, and maintain customer and supplier information. The application is fully containerized with Docker for easy deployment and portability.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
  - [1. Database Setup](#1-database-setup)
  - [2. IDE Setup (NetBeans)](#2-ide-setup-netbeans)
  - [3. Build and Run](#3-build-and-run)
- [Docker Deployment](#docker-deployment)
  - [Prerequisites for Docker](#prerequisites-for-docker)
  - [Running with Docker](#running-with-docker)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)

## Features

*   **User Authentication:** Secure login for system access.
*   **Product Management:** Add, edit, delete, and view products.
*   **Stock Control:** Real-time tracking of inventory levels.
*   **Order Management:** Create and manage customer orders.
*   **Supplier & Customer Tracking:** Maintain a central database of suppliers and customers.
*   **Data Persistence:** All data is stored in a reliable PostgreSQL database.

## Technology Stack

*   **Language:** Java 8
*   **Database:** PostgreSQL
*   **UI Framework:** Java Swing
*   **IDE:** NetBeans
*   **Build Tool:** Apache Ant
*   **Containerization:** Docker

## Prerequisites

Before you begin, ensure you have the following installed:

*   [Java Development Kit (JDK) 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html).
*   [Apache NetBeans IDE](https://netbeans.apache.org/download/index.html).
*   A running instance of [PostgreSQL](https://www.postgresql.org/download/).

## Setup & Installation

Follow these steps to get the project running on your local machine using NetBeans.

### 1. Database Setup

1.  Start your PostgreSQL server.
2.  Using a tool like `pgAdmin` or the `psql` command line, create a new database.
    ```sql
    CREATE DATABASE inventory_db;
    ```
3.  If you have a `.sql` schema file, run it against the `inventory_db` database to create the required tables.

### 2. IDE Setup (NetBeans)

1.  Clone or download this repository.
2.  Open the project in NetBeans IDE (`File -> Open Project...`).
3.  **Add JDBC Driver:** The project references a hardcoded path for the PostgreSQL driver. To fix this:
    *   Create a folder named `lib` in the project's root directory.
    *   Download the PostgreSQL JDBC Driver and place the `.jar` file inside the `lib` folder.
    *   In NetBeans, right-click the **Libraries** folder in the project tree, remove any broken references, and select **Add JAR/Folder**. Navigate to and select the driver `.jar` in your new `lib` folder.
4.  **Configure Database Connection:** Open the `dao.DatabaseConnection.java` file and update the connection string, username, and password to match your local PostgreSQL setup.
5.  **Set Main Class:** The project's default main class might be incorrect. To set it properly:
    *   Right-click the project, go to **Properties**.
    *   In the **Run** category, click **Browse...** next to the "Main Class" field.
    *   Select the class that contains the `main` method to launch the application (e.g., `main.main` or a similar class that creates the main login window).

### 3. Build and Run

1.  **Clean and Build** the project (Right-click project -> "Clean and Build").
2.  **Run** the project (Right-click project -> "Run").

## Docker Deployment

This project includes a `Dockerfile` to run the Java Swing application inside a container. This is an advanced setup that requires an X11 server on the host machine to render the GUI.

> **Note on Java Versions:** The project is built with Java 8, but the `Dockerfile` uses a Java 17 runtime. For maximum compatibility, it is recommended to align these versions.

### Prerequisites for Docker
*   Docker Desktop installed and running.
*   An X11 server for your OS (e.g., VcXsrv for Windows, XQuartz for macOS).

### Running with Docker

1.  **Build the Project:** First, ensure the project is built in NetBeans so that the `dist` directory is created with the application JAR and its libraries.
2.  **Start X11 Server:** Launch your X11 server (e.g., VcXsrv). Ensure that "disable access control" is checked.
3.  **Get Your Host IP:** Find the IP address of your machine on your local network.
4.  **Build the Docker Image:** Open a terminal in the project root and run:
    ```sh
    docker build -t inventory-system .
    ```
5.  **Run the Container:** Run the container, passing the `DISPLAY` environment variable pointing to your host's IP address.
    ```sh
    # Replace YOUR_IP_ADDRESS with your actual IP
    docker run -e DISPLAY=YOUR_IP_ADDRESS:0.0 inventory-system
    ```
The application's GUI should now appear on your desktop, rendered by your X11 server but running inside the Docker container.

## Project Structure

The project follows standard Java conventions and is organized as follows:

```
InventoryManagmentSystem/
├── build/            # Compiled classes (generated by Ant)
├── dist/             # Distribution JAR and libraries (generated by Ant)
├── lib/              # Project dependencies (e.g., postgresql.jar)
├── nbproject/        # NetBeans project configuration
├── src/              # Java source code
│   ├── dao/          # Data Access Objects (for database interaction)
│   ├── model/        # Data models (e.g., Product, User)
│   ├── view/         # GUI components (JFrame, JPanel)
│   └── main/         # Main class to start the application
├── test/             # Unit and integration tests
├── build.xml         # Ant build script
└── Dockerfile        # Docker instructions for containerization
```

## Screenshots



**Login Screen**
![login page.JPG](<login page.JPG>)

**Main Dashboard**
![!Main Dashboard](<dashboard page.JPG>)

**Product Management**
![!Product Management](<product management page.JPG>)


###UML DIAGRAMS

**SEQUENCE DIAGRAM**
![alt text](<sequence diagram.JPG>)

**ACTIVITY DIAGRAM**

![alt text](<activity diagram.png>)

**DATA FLOW DIAGRAM**
![alt text](<data-flow diagram.png>)
