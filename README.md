# Pollymath Academy

A gamified Java learning platform with a wizardry RPG theme. Built with React + Spring Boot + PostgreSQL, fully containerised with Docker.

## Stack

| Layer     | Technology                              |
|-----------|-----------------------------------------|
| Frontend  | React 18, TypeScript, Vite, React Router |
| Backend   | Spring Boot 3.3, Java 21               |
| Database  | PostgreSQL 16                           |
| Auth      | JWT (jjwt 0.12)                        |
| AI Mentor | Anthropic Claude API (proxied via backend) |
| Container | Docker Compose                          |

## Project Structure

```
pollymath-academy/
├── docker-compose.yml
├── .env.example
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── src/
│   │   ├── api/          # Axios client + service functions
│   │   ├── components/   # Nav, CodeEditor, StoryPanel, etc.
│   │   ├── hooks/        # useAuth (context + hook)
│   │   ├── pages/        # Login, Register, Home, Quest
│   │   └── types/        # TypeScript interfaces
└── backend/
    ├── Dockerfile
    └── src/main/java/com/arcane/academy/
        ├── controller/   # Auth, Quest, Code, AiMentor
        ├── service/      # AuthService, QuestService, AiMentorService
        ├── runner/       # JavaCodeRunner (sandboxed JVM execution)
        ├── repository/   # JPA repos for User, Quest, Progress
        ├── model/        # JPA entities
        ├── dto/          # Request/response DTOs
        ├── security/     # JwtService, JwtAuthFilter, UserPrincipal
        └── config/       # SecurityConfig, DataSeeder
```

## Quick Start (Docker)

### 1. Clone and configure

```bash
cp .env.example .env
# Edit .env and set:
#   ANTHROPIC_API_KEY=your_key_here
#   JWT_SECRET=a_random_32+_char_string
```

### 2. Run everything

```bash
docker compose up --build
```

| Service  | URL                    |
|----------|------------------------|
| Frontend | http://localhost:80     |
| Backend  | http://localhost:8080   |
| Database | localhost:5432          |

The database is seeded automatically with 4 quests on first startup.

## Local Development (without Docker)

### Backend

Requirements: Java 21, Maven 3.9+, PostgreSQL running locally

```bash
cd backend

# Set environment variables (or create application-local.yml)
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pollymath_academy
export SPRING_DATASOURCE_USERNAME=pollymath
export SPRING_DATASOURCE_PASSWORD=pollymath_secret
export JWT_SECRET=dev_secret_at_least_32_characters!!
export ANTHROPIC_API_KEY=your_key_here

mvn spring-boot:run
```

### Frontend

Requirements: Node 20+

```bash
cd frontend
npm install
echo "VITE_API_URL=http://localhost:8080" > .env.local
npm run dev
# → http://localhost:5173
```

## API Endpoints

### Auth (public)
| Method | Path               | Description     |
|--------|--------------------|-----------------|
| POST   | /api/auth/register | Register user   |
| POST   | /api/auth/login    | Login, get JWT  |

### Quests (requires JWT)
| Method | Path                       | Description              |
|--------|----------------------------|--------------------------|
| GET    | /api/quests                | All quests with progress |
| GET    | /api/quests/{id}           | Quest detail + story     |
| POST   | /api/quests/{id}/complete  | Mark quest complete      |

### Code (requires JWT)
| Method | Path                      | Description                     |
|--------|---------------------------|---------------------------------|
| POST   | /api/code/run             | Run code, return output         |
| POST   | /api/code/submit/{questId}| Run all test cases + AI feedback|

### AI Mentor (requires JWT)
| Method | Path                 | Description               |
|--------|----------------------|---------------------------|
| POST   | /api/mentor/feedback | Get Socratic hint from AI |

## Architecture Notes

### Code Execution (JavaCodeRunner)
Student code is wrapped in a `StudentSolution` class, compiled in a temp directory using `javax.tools.JavaCompiler`, then executed in an isolated thread with a 5-second timeout. Output is captured from `System.out`. The temp directory is deleted after each run.

> **Production hardening**: For a public deployment, replace the in-process runner with a container-per-run approach (e.g. spawn a Docker container per submission) to fully isolate execution.

### AI Mentor
The backend proxies requests to the Anthropic API, keeping your API key server-side. The prompt instructs Claude to play Master Velan — a Socratic wizard mentor who guides without giving direct answers.

### Database
JPA with `ddl-auto: update` creates tables automatically. The `DataSeeder` bean inserts quests on first startup if the table is empty. Quest test cases are stored as JSONB; expected outputs are never exposed to the frontend.

## Deploying to Cloud

The app is cloud-ready. Example for AWS:

1. Push images to ECR
2. Run with ECS Fargate (or EC2)
3. Use RDS PostgreSQL instead of the db container
4. Set environment variables via ECS task definition / Secrets Manager
5. Put an ALB in front; frontend container serves via nginx

For GCP: Cloud Run (frontend + backend) + Cloud SQL.
For Azure: Container Apps + Azure Database for PostgreSQL.
