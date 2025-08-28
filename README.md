# BookMyScreen - Movie Booking Application

A comprehensive Spring Boot application for managing movie theaters, shows, and bookings with JWT-based authentication and role-based access control.

## 🎬 Features

- **User Management**: User registration and authentication with JWT tokens
- **Movie Management**: CRUD operations for movies with poster uploads
- **Genre Management**: Create and manage movie genres
- **Auditorium Management**: Create auditoriums with automatic seat generation
- **Show Management**: Schedule movie shows with time and pricing
- **Seat Management**: Automatic seat creation and management
- **Role-Based Access**: Admin and User roles with proper authorization
- **File Upload**: Movie poster upload with unique filename generation
- **Pagination**: Efficient data retrieval with pagination support
- **Exception Handling**: Comprehensive error handling with custom exceptions

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.4
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security with method-level authorization
- **Database Migration**: Flyway
- **Build Tool**: Maven
- **Java Version**: 17
- **File Storage**: Local file system for poster uploads

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd bookmyscreen
```

### 2. Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE bookmyscreen;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmyscreen
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📁 Project Structure

```
src/main/java/com/springboot/bookmyscreen/
├── BookmyscreenApplication.java          # Main application class
├── config/
│   └── WebConfig.java                   # Static resource configuration
├── controller/                          # REST controllers
│   ├── AuditoriumController.java
│   ├── GenreController.java
│   ├── MovieController.java
│   ├── SeatController.java
│   ├── ShowController.java
│   └── UserController.java
├── dto/                                 # Data Transfer Objects
│   ├── CreateAuditoriumRequest.java
│   ├── CreateAuditoriumResponse.java
│   ├── CreateGenreRequest.java
│   ├── CreateGenreResponse.java
│   ├── CreateMovieRequest.java
│   ├── CreateMovieResponse.java
│   ├── CreateShowRequest.java
│   ├── CreateShowResponse.java
│   ├── MovieListResponse.java
│   ├── SeatResponse.java
│   ├── UserLoginRequest.java
│   ├── UserLoginResponse.java
│   ├── UserSignupRequest.java
│   └── UserSignupResponse.java
├── entity/                              # JPA entities
│   ├── AuditoriumEntity.java
│   ├── GenreEntity.java
│   ├── MovieEntity.java
│   ├── ReservationEntity.java
│   ├── SeatEntity.java
│   ├── ShowTimeEntity.java
│   └── UserEntity.java
├── enums/                               # Enumerations
│   ├── Genre.java
│   ├── ReservationStatus.java
│   └── SeatStatus.java
├── exception/                           # Custom exceptions
│   ├── CustomAccessDeniedHandlerr.java
│   ├── CustomAuthenticationEP.java
│   ├── DuplicateResourceException.java
│   ├── FileOperationException.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidRequestException.java
│   ├── ResourceNotFoundException.java
│   └── UserNotFoundException.java
├── repository/                          # Data access layer
│   ├── AuditoriumRepository.java
│   ├── GenreRepository.java
│   ├── MovieRepository.java
│   ├── SeatRepository.java
│   ├── ShowRepository.java
│   └── UserRepository.java
├── security/                            # Security configuration
│   ├── JwtFilter.java
│   └── SecurityConfig.java
├── service/                             # Business logic layer
│   ├── AuditoriumService.java
│   ├── CustomUserDetailsService.java
│   ├── FileService.java
│   ├── GenreService.java
│   ├── JwtService.java
│   ├── MovieService.java
│   ├── ShowService.java
│   └── UserService.java
└── util/                                # Utility classes
    └── JWTUtil.java
```

## 🔐 Authentication & Authorization

### User Roles
- **USER**: Can view movies, shows, and auditoriums
- **ADMIN**: Full access to all CRUD operations

### JWT Token Flow
1. User registers/logs in via `/api/users/signup` or `/api/users/login`
2. JWT token is returned in response
3. Include token in subsequent requests: `Authorization: Bearer <token>`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### User Registration
```http
POST /api/users/signup
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### User Login
```http
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Movie Management (Admin Only)

#### Create Movie
```http
POST /api/movies
Content-Type: multipart/form-data
Authorization: Bearer <admin_token>

{
  "name": "Avengers Endgame",
  "description": "A superhero film",
  "poster": <file>,
  "duration": 4800,
  "genres": [1, 2]
}
```

#### Get All Movies (Paginated)
```http
GET /api/movies?page=0&size=10&sortBy=name&sortDir=asc
```

#### Get Movie by ID
```http
GET /api/movies/{id}
```

#### Delete Movie
```http
DELETE /api/movies/{id}
Authorization: Bearer <admin_token>
```

### Genre Management (Admin Only)

#### Create Genre
```http
POST /api/genres
Content-Type: application/json
Authorization: Bearer <admin_token>

{
  "name": "Action"
}
```

### Auditorium Management (Admin Only)

#### Create Auditorium
```http
POST /api/auditoriums
Content-Type: application/json
Authorization: Bearer <admin_token>

{
  "name": "Screen 1",
  "capacity": 100
}
```

#### Get All Auditoriums (Paginated)
```http
GET /api/auditoriums?page=0&size=10&sortBy=name&sortDir=asc
```

#### Get Auditorium by ID
```http
GET /api/auditoriums/{id}
```

#### Delete Auditorium
```http
DELETE /api/auditoriums/{id}
Authorization: Bearer <admin_token>
```

### Show Management (Admin Only)

#### Create Show
```http
POST /api/shows
Content-Type: application/json
Authorization: Bearer <admin_token>

{
  "movieId": 1,
  "auditoriumId": 1,
  "startTime": "2024-01-15T18:00:00",
  "endTime": "2024-01-15T20:30:00",
  "price": 500
}
```

#### Get All Shows (Paginated)
```http
GET /api/shows?page=0&size=10&sortBy=startTime&sortDir=asc
```

#### Get Show by ID
```http
GET /api/shows/{id}
```

#### Delete Show
```http
DELETE /api/shows/{id}
Authorization: Bearer <admin_token>
```

### Seat Management

#### Get Seats by Auditorium
```http
GET /api/seats/auditorium/{auditoriumId}
```

## 🗄️ Database Schema

The application uses Flyway for database migrations. The initial migration creates the following tables:

- `users` - User accounts and authentication
- `genres` - Movie genres
- `movies` - Movie information
- `movie_genres` - Many-to-many relationship between movies and genres
- `auditoriums` - Theater auditoriums
- `seats` - Individual seats in auditoriums
- `showtimes` - Movie show schedules
- `reservations` - Seat reservations (future implementation)

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmyscreen
spring.datasource.username=postgres
spring.datasource.password=admin

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# JWT
security.jwt.secret=replace_with_very_strong_secret_key
security.jwt.expiration=3600000
security.jwt.issuer=example-auth

# File Upload
app.base-url=http://localhost:8080
```

### Environment Variables

For production, set these environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/bookmyscreen
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password
export SECURITY_JWT_SECRET=your_very_strong_secret_key
export APP_BASE_URL=https://your-domain.com
```

## 🚨 Error Handling

The application provides comprehensive error handling with consistent JSON responses:

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "error": "Resource Not Found",
  "message": "Movie not found with id: 999",
  "status": 404
}
```

### Common HTTP Status Codes
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (missing/invalid token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `409` - Conflict (duplicate resource)
- `500` - Internal Server Error

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Manual Testing

1. **Start the application**
2. **Create an admin user** (first user is typically admin)
3. **Test the APIs** using Postman or curl

### Sample Test Flow

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/users/signup \
  -H "Content-Type: application/json" \
  -d '{"name":"Admin User","email":"admin@example.com","password":"admin123"}'

# 2. Login to get JWT token
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'

# 3. Create a genre
curl -X POST http://localhost:8080/api/genres \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{"name":"Action"}'

# 4. Create an auditorium
curl -X POST http://localhost:8080/api/auditoriums \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{"name":"Screen 1","capacity":100}'
```

## 🔒 Security Features

- **JWT Authentication**: Stateless token-based authentication
- **Role-Based Access Control**: Method-level security with `@PreAuthorize`
- **Password Encryption**: BCrypt password hashing
- **Input Validation**: Comprehensive request validation
- **CORS Configuration**: Configurable cross-origin resource sharing
- **CSRF Protection**: Disabled for API endpoints (stateless)

## 📈 Scalability Considerations

- **Database Indexing**: Proper indexes on frequently queried columns
- **Pagination**: Efficient data retrieval for large datasets
- **File Storage**: Scalable file upload system
- **Exception Handling**: Centralized error management
- **Transaction Management**: Proper ACID compliance
- **Connection Pooling**: Optimized database connections

## 🚀 Deployment

### Docker Deployment

1. **Build Docker Image**
```bash
docker build -t bookmyscreen .
```

2. **Run Container**
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/bookmyscreen \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  bookmyscreen
```

### Production Considerations

- Use environment variables for sensitive configuration
- Set up proper logging with log rotation
- Configure database connection pooling
- Set up monitoring and health checks
- Use HTTPS in production
- Implement rate limiting
- Set up backup strategies

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔄 Version History

- **v1.0.0** - Initial release with basic CRUD operations
- **v1.1.0** - Added comprehensive exception handling
- **v1.2.0** - Enhanced security and role-based access control

---

**Note**: This is a development version. For production use, ensure proper security configurations and environment setup. 