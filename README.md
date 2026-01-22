# Pomodoro Spring Application

A complete Pomodoro productivity system built with Java 21, Spring Boot, Clean Architecture, and SOLID principles.

## Features

- **Task Management**: Create, update, complete, and delete tasks
- **Pomodoro Sessions**: Start, pause, resume, complete, and cancel pomodoro sessions
- **XP & Leveling System**: Earn XP for completing pomodoros and tasks
- **Rewards System**: Unlock achievements and badges as you progress

## Architecture

This project follows **Clean Architecture** principles:

```
src/main/java/com/pomodoro/
├── domain/                 # Enterprise Business Rules
│   ├── entity/            # Domain entities (Task, User, PomodoroSession, Reward)
│   ├── repository/        # Repository interfaces
│   └── exception/         # Domain exceptions
├── application/           # Application Business Rules
│   ├── usecase/          # Use cases (TaskUseCase, UserUseCase, PomodoroSessionUseCase)
│   └── dto/              # Data Transfer Objects
├── infrastructure/        # Frameworks & Drivers
│   └── persistence/
│       ├── entity/       # JPA entities
│       ├── repository/   # Repository implementations
│       └── mapper/       # Entity mappers
└── presentation/          # Interface Adapters
    ├── controller/       # REST controllers
    └── exception/        # Exception handlers
```

## SOLID Principles Applied

- **S**ingle Responsibility: Each class has a single purpose
- **O**pen/Closed: Entities are open for extension, closed for modification
- **L**iskov Substitution: Repository implementations can be substituted
- **I**nterface Segregation: Focused interfaces for each repository
- **D**ependency Inversion: Use cases depend on abstractions (repository interfaces)

## Tech Stack

- Java 17+ (compatible with Java 21)
- Spring Boot 3.2.2
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5 & Mockito

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Running the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## API Endpoints

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| POST | `/api/users/{id}/add-pomodoro-xp` | Add XP for completing a pomodoro |
| POST | `/api/users/{id}/add-task-xp` | Add XP for completing a task |
| DELETE | `/api/users/{id}` | Delete a user |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks/{id}` | Get task by ID |
| GET | `/api/tasks/user/{userId}` | Get all tasks for a user |
| GET | `/api/tasks/user/{userId}/pending` | Get pending tasks |
| GET | `/api/tasks/user/{userId}/completed` | Get completed tasks |
| PUT | `/api/tasks/{id}` | Update a task |
| POST | `/api/tasks/{id}/complete` | Mark task as complete |
| DELETE | `/api/tasks/{id}` | Delete a task |

### Pomodoro Sessions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pomodoro/start` | Start a new pomodoro session |
| POST | `/api/pomodoro/{id}/pause` | Pause a session |
| POST | `/api/pomodoro/{id}/resume` | Resume a paused session |
| POST | `/api/pomodoro/{id}/complete` | Complete a session |
| POST | `/api/pomodoro/{id}/cancel` | Cancel a session |
| GET | `/api/pomodoro/{id}` | Get session by ID |
| GET | `/api/pomodoro/user/{userId}` | Get all sessions for a user |
| GET | `/api/pomodoro/task/{taskId}` | Get all sessions for a task |

## Configuration

Configuration can be modified in `application.properties`:

```properties
# Pomodoro durations (in minutes)
pomodoro.default-work-minutes=25
pomodoro.default-break-minutes=5
pomodoro.default-long-break-minutes=15

# XP rewards
pomodoro.xp-per-pomodoro=10
pomodoro.xp-per-task-completed=25
```

## Example Usage

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "email": "john@example.com"}'
```

### Create a Task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Study Java", "description": "Learn Spring Boot", "estimatedPomodoros": 4, "userId": "USER_UUID"}'
```

### Start a Pomodoro Session

```bash
curl -X POST http://localhost:8080/api/pomodoro/start \
  -H "Content-Type: application/json" \
  -d '{"userId": "USER_UUID", "taskId": "TASK_UUID", "sessionType": "WORK"}'
```

## H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:pomodorodb`
- Username: `sa`
- Password: (empty)

## License

This project is open source and available under the MIT License.