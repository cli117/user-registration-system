# User Registration and Authentication System

This project implements a simple user registration and authentication system using a token-based approach.

## Technologies Used

* **Java:** Programming language
* **Spring Boot:** Framework for building the application
* **Spring Security:** Security framework for authentication and authorization
* **Spring Data JPA (Hibernate):** ORM for database interaction
* **MySQL:** Relational database for storing user data
* **Redis:** In-memory data store for storing authentication tokens
* **JUnit 5:** Framework for writing unit tests
* **Mockito:** Mocking framework for unit tests
* **AssertJ:** Fluent assertion library for Java tests

## Prerequisites

Make sure you have the following installed on your system:

* **Java Development Kit (JDK):** JDK 23 recommended. To use a different JDK version, please update the configuration in pom.xml
* **Maven or Gradle:** Build automation tool.
* **MySQL:** Relational database.
* **Redis:** In-memory data store.

## Setup Instructions

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd user-registration-system
    ```

2.  **Build the project using Maven (if you are using Maven):**
    ```bash
    mvn clean install
    ```
    Or using Gradle (if you are using Gradle):
    ```bash
    ./gradlew clean build
    ```

3.  **Configure the application:**
    * Edit the `application.yml` file in the `src/main/resources` directory.
    * Configure your database connection details (e.g., MySQL host, port, username, password, database name).
    * Configure your Redis connection details (e.g., Redis host, port).

    **Example `application.properties`:**
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?serverTimezone=UTC
    spring.datasource.username=your_database_username
    spring.datasource.password=your_database_password
    spring.jpa.hibernate.ddl-auto=create-drop
    spring.redis.host=localhost
    spring.redis.port=6379
    ```

    **Note:** The `spring.jpa.hibernate.ddl-auto` property is currently set to `create-drop`. This setting will automatically create the database schema on application startup and drop it when the application shuts down. **This is generally used for testing and development purposes and is NOT recommended for production environments as it will erase your data.**

    For other environments, you might want to use different values for `spring.jpa.hibernate.ddl-auto`, such as:
    * `update`: Updates the schema based on the entities, without dropping existing data.
    * `validate`: Validates the schema against the entities and throws an exception if there are discrepancies.
    * `none`: Does not perform any schema management.

    Choose the appropriate value for your specific environment.

4.  **Run the application:**
    * Using Maven:
        ```bash
        mvn spring-boot:run
        ```
    * Using Gradle:
        ```bash
        ./gradlew bootRun
        ```

## API Endpoints

* **`POST /api/auth/signup`**: Registers a new user.
    * **Request Body (JSON):**
        ```json
        {
            "username": "your_username",
            "password": "your_password",
            "email": "[youremail@gmail.com]"
        }
        ```
    * **Response Body (JSON):** Returns a `SignupResponse` indicating success or failure.

* **`POST /api/auth/login`**: Logs in an existing user.
    * **Request Body (JSON):**
        ```json
        {
            "usernameOrEmail": "your_username_or_email",
            "password": "your_password"
        }
        ```
    * **Response Body (JSON):** Returns a `LoginResponse` containing a `token` upon successful login.

* **`GET /api/auth/protected`**: An example protected resource that requires authentication.
    * **Request Headers:** Requires an `Authorization` header with the format `Bearer <your_token>`.
    * **Response Body:** Returns a success message if the token is valid.

## Running Tests

To run the unit tests, execute the following command from the project root directory:

* Using Maven:
    ```bash
    mvn test
    ```
* Using Gradle:
    ```bash
    ./gradlew test
    ```

---

Feel free to modify this README to better reflect the specifics of your project. You might want to add information about any specific configurations or further steps needed.
