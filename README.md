# Arcane Academy

A gamified, polymathic learning platform with a wizardry RPG aesthetic. Learners progress through structured content tiers, earn XP and badges, and are guided by an AI mentor. Topics include Java, React, Tailwind CSS, Psychology, Natural Sciences, and Genealogy — with more planned.

> **For engineers:** see [`PROJECT_REFERENCE.md`](PROJECT_REFERENCE.md) for architecture, decision log, content system, and deployment details.

---

## Stack

| Layer       | Technology                                                    |
|-------------|---------------------------------------------------------------|
| Frontend    | React 18, TypeScript, Vite, React Router, Tailwind CSS, Shadcn/ui |
| Backend     | Spring Boot 3.3, Java 21, Spring Modulith (package-by-feature) |
| Database    | PostgreSQL (Neon serverless)                                  |
| Auth        | JWT + Google OAuth2                                           |
| AI Mentor   | Anthropic Claude API (proxied via backend)                    |
| Payments    | Stripe                                                        |
| Deployment  | Google Cloud Run (backend) + Netlify (frontend)               |

---

## Quick Start (Docker — local staging)

Docker Compose runs the full stack locally with Flyway migrations and prod-like schema behaviour. Use this to verify changes before pushing to master.

### 1. Configure environment

```bash
cp .env.example .env
# Fill in: JWT_SECRET, ANTHROPIC_API_KEY, STRIPE_SECRET_KEY (test key),
#          GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, and other values.
# See PROJECT_REFERENCE.md §2 for the full variable list.
```

### 2. Run

```bash
docker compose up --build
```

| Service  | URL                   |
|----------|-----------------------|
| Frontend | http://localhost      |
| Backend  | http://localhost:8080 |
| Database | localhost:5432        |

Content is seeded automatically from `backend/src/main/resources/content/` on first startup.

### 3. Clean-slate run (wipe DB + re-run all migrations)

Run this before any PR that adds a new Flyway migration:

```bash
docker compose down -v && docker compose up --build
```

### Stripe webhooks (local)

Stripe cannot reach `localhost` directly. Use the Stripe CLI to forward events:

```bash
stripe listen --forward-to http://localhost:8080/api/payments/webhook
```

Copy the printed `whsec_...` signing secret into `.env` as `STRIPE_WEBHOOK_SECRET`.

---

## Local Development (without Docker)

### Backend

Requirements: Java 21, Maven 3.9+, PostgreSQL running locally

```bash
cd backend
mvn spring-boot:run
```

The base `application.yml` defaults to `localhost:5432/arcane_academy` with `ddl-auto: update` — no extra config needed for a basic dev run.

### Frontend

Requirements: Node 20+

```bash
cd frontend
npm install
npm run dev   # → http://localhost:5173
```

The Vite dev server proxies `/api/*` to `http://localhost:8080` by default.

---

## Git Workflow

```
feature/* → docker compose up --build (verify) → merge to master → CI → manual prod deploy
```

CI runs on every push and PR (`mvn verify`, lint, type-check, Vitest, Vite build). Production is deployed manually via `gcloud run deploy` — never automatically on push to master.
