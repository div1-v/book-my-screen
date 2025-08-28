# BookMyScreen - Movie Booking System

A Spring Boot application for movie theater management with seat booking, JWT authentication, and role-based access control.

## 🎬 Features

- **User Management**: Registration, login with JWT authentication
- **Movie Management**: CRUD operations with poster uploads
- **Auditorium & Seats**: Automatic seat generation and management
- **Show Scheduling**: Create and manage movie shows
- **Seat Booking**: Real-time seat reservation with double-booking prevention
- **Role-Based Access**: Admin and User roles with proper authorization
- **File Upload**: Movie poster storage with unique filenames

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.4, Java 17
- **Database**: PostgreSQL with Flyway migrations
- **Security**: JWT + Spring Security
- **Build**: Maven

## 🚀 Quick Start

### Prerequisites
- Java 17+, Maven 3.6+, PostgreSQL 12+

### Setup
```bash
# Clone & navigate
git clone <repository-url>
cd bookmyscreen

# Database setup
CREATE DATABASE bookmyscreen;

# Configure database in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmyscreen
spring.datasource.username=your_username
spring.datasource.password=your_password

# Build & run
mvn clean install
mvn spring-boot:run
```

Application runs on `http://localhost:8080`

## 🔐 Authentication

### User Registration
```http
POST /api/users/signup
{
  "name": "John Doe",
  "email": "john@example.com", 
  "password": "password123"
}
```

### User Login
```http
POST /api/users/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

Include JWT token: `Authorization: Bearer <token>`

## 📚 Key APIs

### Movies (Admin)
```http
POST /api/movies          # Create movie with poster
GET /api/movies           # List movies (paginated)
DELETE /api/movies/{id}   # Delete movie
```

### Shows (Admin)
```http
POST /api/shows           # Schedule show
GET /api/shows            # List shows
```

### Reservations (User)
```http
POST /api/reservations    # Book seats
GET /api/reservations/available-seats/{showId}  # Check availability
DELETE /api/reservations/{id}                   # Cancel booking
```

### Auditoriums (Admin)
```http
POST /api/auditoriums     # Create auditorium
GET /api/auditoriums      # List auditoriums
```

## 🔒 Double Booking Prevention

- **Database Constraint**: Unique constraint on `(showtime_id, seat_id)`
- **Pessimistic Locking**: Row-level locks during booking
- **Application Validation**: Pre-booking seat availability checks
- **Atomic Transactions**: All-or-nothing reservation creation

## 🗄️ Database Schema

- `users` - User accounts
- `movies` - Movie information  
- `auditoriums` - Theater screens
- `seats` - Individual seats
- `showtimes` - Movie schedules
- `reservations` - Seat bookings
- `genres` - Movie categories

## 🔧 Configuration

Key properties in `application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmyscreen

# JWT
security.jwt.secret=your_secret_key
security.jwt.expiration=3600000

# File Upload
app.base-url=http://localhost:8080
```

## 🧪 Testing

```bash
# Run tests
mvn test

# Manual testing flow
1. Register user → Get JWT token
2. Create genre, auditorium, movie, show (Admin)
3. Book seats (User)
4. Check availability
```

## 🚀 Deployment

### Docker
```bash
docker build -t bookmyscreen .
docker run -p 8080:8080 bookmyscreen
```

### Production
- Set environment variables for database and JWT
- Use HTTPS
- Configure proper logging
- Set up monitoring

## 📄 License

MIT License

---

**Note**: Development version. Configure security properly for production use. 