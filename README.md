# Inventory Management System

Short description
This repository contains an Inventory Management System (Java, Ant, NetBeans project). The app follows an MVC structure with packages: model, dao, controller, view.

Status
- Project structure in place (src packages).
- Build system: Ant (build.xml).
- Missing/To do: add diagrams (Activity, Data Flow/ER, Sequence), unit tests (JUnit), Dockerization (Dockerfile included here), complete implementation of controllers/views, add CI for tests.

Quickstart (build + run)
1. Build with Ant:
   - ant clean
   - ant jar
   - The generated jar will be in the `dist/` directory (if build.xml configured to produce it).

2. Run:
   - java -jar dist/InventoryManagementSystem.jar
   - Or run from NetBeans as a Java project.

Testing
- Unit tests should be placed under `test/` and use JUnit 5.
- To run tests: configure Ant to run tests or use an IDE runner.

Diagrams and design documents
- Add the following to the repo:
  - diagrams/activity_diagram.png or .puml
  - diagrams/data_flow_or_er.png or .puml
  - diagrams/sequence_diagram.png or .puml
  - slides/ (PowerPoint summarizing title, problem, and 3 diagrams)

Coding standards
- Follow Google Java Style Guide.
- Use Checkstyle and SpotBugs to enforce code quality.

Design patterns
- Use DAO for persistence (package `dao` exists).
- Use a Singleton for DB connection management (or a connection pool).
- Consider a Service layer for business logic between controllers and DAO.

Docker
- A Dockerfile is provided to build a container image for the application (see Dockerfile).

What to deliver for the exam
- Project with source code and clean commits.
- 3 diagrams and a short PPT summarizing the project.
- Unit tests with passing results.
- Docker image or instructions to run via Docker.
- Short test plan / test cases document.

If you want, I can:
- Expand this README in the repo,
- Create a Dockerfile and add it,
- Generate starter JUnit test(s),
- Create PlantUML templates for your diagrams,
- Or produce a short PPT template for the slides.

Tell me which of the above I should prepare first. 
