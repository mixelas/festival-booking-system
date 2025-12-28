# Festival Booking Management System

A comprehensive Spring Boot application for managing music festivals, performances, and artist bookings with JWT-based authentication.

## Features

- **User Authentication**: Secure JWT-based authentication with role-based access control
- **Festival Management**: Create, update, and browse music festivals
- **Performance Booking**: Artists can submit and manage their performances
- **Role-Based Access**: Support for multiple roles (Admin, Organizer, Staff, Artist, User)
- **Search & Pagination**: Find festivals and performances with pagination support
- **Responsive UI**: Modern, dark-themed web interface
- **Technical Requirements**: Manage setup requirements, merchandise, and rehearsal slots

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5
- **Database**: H2 (in-memory for development)
- **Security**: JWT (JJWT 0.11.5), Spring Security
- **Build**: Maven
- **Frontend**: HTML5, CSS3, Vanilla JavaScript

## Project Structure

```
src/main/
├── java/com/example/festival_management/
│   ├── controller/          # REST API endpoints
│   ├── service/             # Business logic
│   ├── repository/          # Data access layer
│   ├── entity/              # JPA entities
│   ├── config/              # Security and web configuration
│   ├── security/            # JWT utilities and filters
│   ├── exception/           # Exception handlers
│   └── util/                # Helper utilities
└── resources/
    ├── static/              # Frontend HTML, CSS, JS
    ├── application.properties
    └── data.sql
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd festival-booking-system
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`

### Configuration

Edit `src/main/resources/application.properties` for custom settings:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expirationMs=3600000
```

## API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user (requires auth)

### Festivals

- `GET /api/festivals` - List festivals (paginated, searchable)
- `GET /api/festivals/{id}` - Get festival details
- `POST /api/festivals` - Create new festival (requires auth)

### Performances

- `POST /api/performances/festival/{festivalId}` - Submit new performance
- `GET /api/performances` - List all performances
- `GET /api/performances/{id}` - Get performance details

### Users

- `GET /api/users` - List all users
- `GET /api/users/{username}` - Get user by username
- `GET /api/users/exists/username/{username}` - Check username availability
- `GET /api/users/exists/email/{email}` - Check email availability

## Authentication

The API uses Bearer token authentication. Include the JWT token in the Authorization header:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/auth/me
```

### Login Example

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"artist1","password":"password123"}'
```

Response:
```json
{
  "accessToken": "eyJhbGci...",
  "token": "eyJhbGci...",
  "roles": ["ROLE_ARTIST"]
}
```

## Database Schema

The application uses JPA with automatic schema generation. Key tables:

- **users**: User accounts and authentication
- **festivals**: Festival events
- **performances**: Artist performances at festivals
- **role_assignments**: User role mappings
- **reviews**: Performance reviews

## Development

### Running Tests

```bash
mvn test
```

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

Default credentials:
- URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (empty)

## Security

- Passwords are hashed using BCrypt
- JWT tokens expire after 1 hour (configurable)
- CSRF protection is enabled for state-changing operations
- Role-based authorization on protected endpoints

### Development Note

Debug settings are currently enabled for development. For production, disable detailed error messages:

```properties
server.error.include-stacktrace=on_param
logging.level.root=INFO
```

## Future Enhancements

- [ ] Email notifications for performance submissions
- [ ] Payment integration for ticket sales
- [ ] Real-time chat between organizers and artists
- [ ] Advanced analytics and reporting
- [ ] Mobile app support
- [ ] External database (PostgreSQL) support
- [ ] CI/CD pipeline integration

## License

This project is open source and available under the MIT License.

## Support

For questions or issues, please create an issue in the repository.

---

Built with ❤️ for festival management
