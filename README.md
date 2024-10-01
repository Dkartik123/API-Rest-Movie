# Movie Database API

## Overview

The **Movie Database API** is a RESTful web service built using **Spring Boot** and **JPA**. It allows users to manage a comprehensive database of movies, genres, and actors. The API supports full **CRUD** operations for each entity and provides advanced features such as filtering, searching, pagination, and handling complex relationships between entities.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Requirements](#requirements)
- [Installation and Setup](#installation-and-setup)
- [Database Initialization](#database-initialization)
- [API Endpoints](#api-endpoints)
    - [Genre Endpoints](#genre-endpoints)
    - [Movie Endpoints](#movie-endpoints)
    - [Actor Endpoints](#actor-endpoints)
- [Sample Data](#sample-data)
- [Testing the API](#testing-the-api)
- [Error Handling and Validation](#error-handling-and-validation)
- [Future Enhancements](#future-enhancements)
- [License](#license)
- [Contact Information](#contact-information)

## Features

- **CRUD Operations**: Create, read, update, and delete genres, movies, and actors.
- **Relationships**:
    - **Many-to-Many**: Between movies and actors.
    - **Many-to-One**: Between movies and genres.
- **Filtering and Searching**:
    - Filter movies by genre, release year, or actor.
    - Search movies by title (case-insensitive and partial matches).
    - Search actors by name (case-insensitive).
- **Pagination**: Implemented for GET requests returning multiple entities.
- **Input Validation**: Ensures data integrity using Bean Validation annotations.
- **Error Handling**: Comprehensive error responses with appropriate HTTP status codes.
- **Cascade Operations**: Optionally delete associated movies when deleting a genre.
- **Data Format**: Dates handled in ISO 8601 format.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **SQLite** (Database)
- **Maven**
- **Lombok** (to reduce boilerplate code)
- **Postman** (for API testing)

## Requirements

- **Java 17** or higher
- **Maven 3.8.x** or higher
- **SQLite JDBC Driver**
- **Postman** (optional, for testing)

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://your-repository-url.git
cd movie-database-api
```

### 2. Build the Project

Use Maven to build the project:

```bash
mvn clean install
```

### 3. Configure the Database

The project uses SQLite as its database. The database configuration is located in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlite:movie_database.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

- **Note**: The `ddl-auto=update` setting ensures that the database schema is automatically updated based on your entity classes.

### 4. Run the Application

You can run the application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, you can use your IDE to run the `MovieApplication` class as a Java application.

### 5. Access the API

Once the application is running, the API will be available at:

```
http://localhost:8080/api
```

## Database Initialization

To demonstrate the functionality, you can populate the database with sample data.

### Using SQL Scripts

Create SQL scripts to insert sample data into your database.

#### 1. Insert Genres

```sql
INSERT INTO genres (name) VALUES ('Action');
INSERT INTO genres (name) VALUES ('Drama');
INSERT INTO genres (name) VALUES ('Comedy');
INSERT INTO genres (name) VALUES ('Thriller');
INSERT INTO genres (name) VALUES ('Science Fiction');
```

#### 2. Insert Actors

```sql
INSERT INTO actors (name, birth_date) VALUES ('Leonardo DiCaprio', '1974-11-11');
INSERT INTO actors (name, birth_date) VALUES ('Brad Pitt', '1963-12-18');
INSERT INTO actors (name, birth_date) VALUES ('Scarlett Johansson', '1984-11-22');
-- Add more actors as needed
```

#### 3. Insert Movies

```sql
INSERT INTO movies (title, release_year, duration, genre_id) VALUES ('Inception', 2010, 148, 1);
INSERT INTO movies (title, release_year, duration, genre_id) VALUES ('The Matrix', 1999, 136, 1);
-- Add more movies as needed
```

#### 4. Associate Movies and Actors

```sql
INSERT INTO movie_actor (movie_id, actor_id) VALUES (1, 1); -- Inception -> Leonardo DiCaprio
INSERT INTO movie_actor (movie_id, actor_id) VALUES (2, 2); -- The Matrix -> Keanu Reeves
-- Add more associations as needed
```

### Using the API

Alternatively, you can use **Postman** to send POST requests to the API endpoints to create genres, movies, and actors.

## API Endpoints

### Genre Endpoints

- **Create a Genre**

  ```
  POST /api/genres
  ```

  **Request Body**:

  ```json
  {
    "name": "Action"
  }
  ```

- **Get All Genres**

  ```
  GET /api/genres
  ```

- **Get Genre by ID**

  ```
  GET /api/genres/{id}
  ```

- **Update a Genre**

  ```
  PATCH /api/genres/{id}
  ```

  **Request Body**:

  ```json
  {
    "name": "Adventure"
  }
  ```

- **Delete a Genre**

  ```
  DELETE /api/genres/{id}?cascade=true
  ```

    - **cascade=true**: Deletes the genre and all associated movies.
    - **cascade=false**: Deletes the genre only if no movies are associated.

### Movie Endpoints

- **Create a Movie**

  ```
  POST /api/movies
  ```

  **Request Body**:

  ```json
  {
    "title": "Interstellar",
    "releaseYear": 2014,
    "duration": 169,
    "genre": {
      "id": 5
    },
    "actors": [
      {
        "id": 1
      },
      {
        "id": 3
      }
    ]
  }
  ```

- **Get All Movies**

  ```
  GET /api/movies
  ```

    - Supports pagination:

      ```
      GET /api/movies?page=0&size=10
      ```

    - Supports filtering:

      ```
      GET /api/movies?genre=1
      GET /api/movies?year=2010
      GET /api/movies?actor=2
      ```

- **Get Movie by ID**

  ```
  GET /api/movies/{id}
  ```

- **Update a Movie**

  ```
  PATCH /api/movies/{id}
  ```

  **Request Body**:

  ```json
  {
    "title": "Interstellar: The Journey Continues",
    "actors": [
      {
        "id": 4
      }
    ]
  }
  ```

- **Delete a Movie**

  ```
  DELETE /api/movies/{id}
  ```

- **Search Movies by Title**

  ```
  GET /api/movies/search?title=interstellar
  ```

### Actor Endpoints

- **Create an Actor**

  ```
  POST /api/actors
  ```

  **Request Body**:

  ```json
  {
    "name": "Matthew McConaughey",
    "birthDate": "1969-11-04"
  }
  ```

- **Get All Actors**

  ```
  GET /api/actors
  ```

    - Supports searching by name:

      ```
      GET /api/actors?name=matthew
      ```

- **Get Actor by ID**

  ```
  GET /api/actors/{id}
  ```

- **Update an Actor**

  ```
  PATCH /api/actors/{id}
  ```

  **Request Body**:

  ```json
  {
    "name": "Matthew McConaughey Jr."
  }
  ```

- **Delete an Actor**

  ```
  DELETE /api/actors/{id}?cascade=true
  ```

    - **cascade=true**: Removes the actor from all associated movies without deleting the movies.
    - **cascade=false**: Deletes the actor only if not associated with any movies.

## Sample Data

### Genres

1. **Action**
2. **Drama**
3. **Comedy**
4. **Thriller**
5. **Science Fiction**

### Movies

- **Inception** (2010)
- **The Matrix** (1999)
- **Interstellar** (2014)
- **Pulp Fiction** (1994)
- **The Dark Knight** (2008)
- **Fight Club** (1999)
- **Forrest Gump** (1994)
- **The Wolf of Wall Street** (2013)
- **Get Out** (2017)
- **Parasite** (2019)
- **Mad Max: Fury Road** (2015)
- **Avengers: Endgame** (2019)
- **Django Unchained** (2012)
- **Whiplash** (2014)
- **1917** (2019)
- **Logan** (2017)
- **Spider-Man: No Way Home** (2021)
- **Joker** (2019)
- **Guardians of the Galaxy** (2014)
- **The Grand Budapest Hotel** (2014)

### Actors

1. **Leonardo DiCaprio**
2. **Brad Pitt**
3. **Scarlett Johansson**
4. **Robert Downey Jr.**
5. **Tom Hanks**
6. **Margot Robbie**
7. **Christian Bale**
8. **Matthew McConaughey**
9. **Joaquin Phoenix**
10. **Emma Stone**
11. **Daniel Craig**
12. **Chris Hemsworth**
13. **Tom Hardy**
14. **Natalie Portman**
15. **Will Smith**

## Testing the API

### Using Postman

A **Postman** collection named **Movie Database API** is available in the repository. Import it into Postman to test the API endpoints easily.

### Example Requests

#### 1. Create a New Genre

```http
POST /api/genres
Content-Type: application/json

{
  "name": "Adventure"
}
```

#### 2. Get Movies by Genre

```http
GET /api/movies?genre=1
```

#### 3. Search Movies by Title

```http
GET /api/movies/search?title=matrix
```

#### 4. Update an Actor's Name

```http
PATCH /api/actors/1
Content-Type: application/json

{
  "name": "Leonardo DiCaprio Jr."
}
```

#### 5. Delete a Genre with Cascade

```http
DELETE /api/genres/1?cascade=true
```

## Error Handling and Validation

- **404 Not Found**: Returned when a requested resource does not exist.
- **400 Bad Request**: Returned when validation fails or input data is invalid.
- **Validation Annotations**:
    - `@NotBlank`: Ensures that a string is not null or empty.
    - `@Past`: Validates that a date is in the past.
    - `@Min` and `@Max`: Checks numerical constraints.
- **Exception Handling**:
    - A `@ControllerAdvice` class handles exceptions globally and returns meaningful error responses.

### Example: Handling Invalid Birth Date

```http
POST /api/actors
Content-Type: application/json

{
  "name": "John Doe",
  "birthDate": "2025-01-01"
}
```

**Response**:

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "timestamp": "2024-10-01T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Birth date must be in the past",
  "path": "/api/actors"
}
```

## Future Enhancements

- **Authentication and Authorization**: Implement security measures using Spring Security.
- **Swagger Documentation**: Integrate Swagger for API documentation.
- **Bulk Operations**: Add endpoints for bulk creation and updates.
- **Advanced Search**: Implement more complex search queries and filters.
- **Internationalization**: Support multiple languages for data input and output.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact Information

For questions or support, please contact:

- **Name**: Your Name
- **Email**: your.email@example.com
- **GitHub**: [your-github-username](https://github.com/your-github-username)

---

**Note**: Replace placeholders like `https://your-repository-url.git`, `Your Name`, and `your.email@example.com` with your actual information.