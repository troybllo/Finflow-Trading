# FinFlow Trading

**A full-stack trading journal and portfolio management platform with social features, multi-broker integration, and AI-powered analytics.**

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.2.0-61DAFB?style=flat&logo=react&logoColor=black)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9.3-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.1-06B6D4?style=flat&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)

---

## Overview

FinFlow Trading is an enterprise-grade trading journal application designed for traders who manage multiple brokerage accounts and want to track their performance holistically. Unlike traditional trading journals, FinFlow aggregates data across all connected accounts to provide a unified view of your **total P&L, win rate, and portfolio performance**.

### Key Differentiators

- **Unified Multi-Account View**: See combined P&L across Alpaca, Robinhood, Coinbase, Binance, Interactive Brokers, and TD Ameritrade
- **Social Trading Leaderboard**: Compare performance with friends (with consent) - view their trades, P&L, and strategies
- **AI-Powered Insights**: Intelligent trade analysis and pattern recognition using RAG-powered market analysis
- **Comprehensive Journaling**: Document trade rationale, emotions, and outcomes for continuous improvement

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT LAYER                                    │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  React 19 + TypeScript + Tailwind CSS + Recharts                    │    │
│  │  Dashboard | Portfolio | Trading | Leaderboard | Analytics          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           MICROSERVICES LAYER                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ API Gateway  │  │  Portfolio   │  │   Trading    │  │  Analytics   │    │
│  │   (Planned)  │  │   Service    │  │    Engine    │  │   Service    │    │
│  │              │  │    ✅ Live   │  │   (Planned)  │  │   (Planned)  │    │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘    │
│                           │                                                  │
│                    Protocol Buffers / gRPC                                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                             DATA LAYER                                       │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PostgreSQL (Production) | H2 (Development)                         │    │
│  │  Users → Portfolios → Holdings → Orders → Transactions              │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Design Patterns & Principles

- **Layered Architecture**: Controllers → Services → Repositories → Entities
- **Domain-Driven Design**: Rich domain models with business logic encapsulated in entities
- **Repository Pattern**: Spring Data JPA with 150+ custom query methods for complex financial analytics
- **DTO Pattern**: Request/Response DTOs for API contract stability and validation
- **Global Exception Handling**: Centralized error handling with standardized API error responses

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core language with modern features (records, sealed classes) |
| Spring Boot | 3.2.1 | REST API framework with auto-configuration |
| Spring Data JPA | 3.2.1 | ORM with Hibernate for database operations |
| Spring Security | 6.x | Authentication & BCrypt password hashing |
| PostgreSQL | 16 | Production database with ACID compliance |
| H2 Database | 2.x | In-memory database for development/testing |
| Maven | 3.9+ | Dependency management and build automation |
| Protocol Buffers | 3.x | Inter-service communication schema |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 19.2.0 | UI framework with concurrent features |
| TypeScript | 5.9.3 | Type-safe JavaScript |
| Vite | 7.2.4 | Next-gen build tool with HMR |
| Tailwind CSS | 4.1.18 | Utility-first styling |
| React Router | 7.11.0 | Client-side routing |
| Axios | 1.13.2 | HTTP client with interceptors |
| Recharts | 3.6.0 | Composable charting library |

### Infrastructure
| Technology | Purpose |
|------------|---------|
| Docker Compose | Multi-container orchestration |
| Terraform | Infrastructure as Code |
| gRPC | High-performance inter-service RPC |

---

## Features

### Portfolio Management
- **Unified Dashboard**: Real-time portfolio value, daily P&L, and performance metrics
- **Holdings Tracking**: Monitor positions across asset types (stocks, crypto, forex, options, futures)
- **Performance History**: Historical performance charts with customizable timeframes

### Multi-Broker Integration
Connect and sync data from major brokerages:
- Alpaca
- Robinhood
- Coinbase
- Binance
- Interactive Brokers
- TD Ameritrade

**OAuth-based authentication** with automatic token refresh and sync status monitoring.

### Social Features
- **Leaderboard**: Ranked performance comparison with friends
- **Friend System**: Send/accept friend requests with granular privacy controls
- **Activity Feed**: See friends' recent trades and achievements (with consent)
- **Profile Sharing**: Choose what metrics to share publicly

### Trade Journaling
- **Entry Documentation**: Record entry/exit rationale for every trade
- **Emotion Tracking**: Tag trades with emotional state (FOMO, confident, fearful, etc.)
- **Trade Analysis**: AI-powered feedback on trade quality and patterns
- **Tagging System**: Organize trades by strategy, setup type, or custom tags

### Analytics & AI Insights
- **Performance Metrics**: Win rate, average R-multiple, Sharpe ratio, max drawdown
- **AI Analysis**: Pattern recognition and trade quality scoring
- **Risk Assessment**: Portfolio risk analysis with correlation metrics
- **Market Insights**: RAG-powered market analysis and news summarization

---

## Data Model

```
┌──────────────────┐       ┌──────────────────┐       ┌──────────────────┐
│      User        │       │    Portfolio     │       │     Holding      │
├──────────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (UUID)        │──1:1──│ id (UUID)        │──1:N──│ id (UUID)        │
│ email            │       │ totalValue       │       │ symbol           │
│ username         │       │ cashBalance      │       │ quantity         │
│ passwordHash     │       │ buyingPower      │       │ averageCost      │
│ avatar           │       │ dailyChange      │       │ currentPrice     │
│ createdAt        │       │ totalGainLoss    │       │ unrealizedPnL    │
│ updatedAt        │       │ totalGainLoss%   │       │ assetType        │
└──────────────────┘       └──────────────────┘       └──────────────────┘
         │
         │ 1:N
         ▼
┌──────────────────┐       ┌──────────────────┐
│ ExternalAccount  │       │      Order       │
├──────────────────┤       ├──────────────────┤
│ id (UUID)        │       │ id (UUID)        │
│ platform         │       │ symbol           │
│ accountName      │       │ side (BUY/SELL)  │
│ status           │       │ type (MARKET/..) │
│ accessToken      │       │ quantity         │
│ lastSyncAt       │       │ status           │
│ syncEnabled      │       │ filledAt         │
└──────────────────┘       └──────────────────┘
```

### Key Design Decisions

1. **UUID Primary Keys**: Enables distributed system scalability without coordination
2. **BigDecimal for Financials**: Ensures precision for monetary calculations (4 decimal places)
3. **Denormalized userId**: Added to Holding/Order entities for optimized query performance
4. **Cascade Deletes**: Portfolio removed when user deleted; holdings removed with portfolio
5. **Eager Join Fetches**: Custom JPQL queries prevent N+1 query problems
6. **Comprehensive Indexing**: 15+ indexes on foreign keys and frequently queried columns

---

## Project Structure

```
finflow-trading/
├── finflow/
│   ├── frontend/my-app/              # React SPA
│   │   └── src/
│   │       ├── pages/                # Route components
│   │       │   ├── Dashboard.tsx
│   │       │   ├── Portfolio.tsx
│   │       │   ├── Trading.tsx
│   │       │   ├── Leaderboard.tsx
│   │       │   └── Analytics.tsx
│   │       ├── components/layout/    # Shared UI components
│   │       ├── services/api.ts       # Axios API client (~1200 LOC)
│   │       └── types/index.ts        # TypeScript interfaces
│   │
│   ├── services/
│   │   └── portfolio-service/        # Spring Boot microservice
│   │       └── src/main/java/com/finflow/portfolio/
│   │           ├── controller/       # REST endpoints
│   │           ├── service/          # Business logic
│   │           ├── repository/       # Data access (150+ queries)
│   │           ├── entity/           # JPA entities
│   │           ├── dto/              # Request/Response objects
│   │           ├── exception/        # Custom exceptions
│   │           └── config/           # Security, beans
│   │
│   ├── infrastructure/
│   │   ├── database/
│   │   │   ├── schema.sql            # DDL statements
│   │   │   ├── indexes.sql           # Performance indexes
│   │   │   └── seed.sql              # Sample data
│   │   ├── terraform/                # IaC configuration
│   │   └── docker-compose.yml
│   │
│   ├── proto/
│   │   └── trading.proto             # gRPC service definitions
│   │
│   └── docs/
│       ├── api/                      # OpenAPI documentation
│       └── architecture/             # Design documents
│
└── README.md
```

---

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.9+
- Docker & Docker Compose (optional)
- PostgreSQL 16 (or use H2 for development)

### Backend Setup

```bash
# Navigate to portfolio service
cd finflow/services/portfolio-service

# Build the project
mvn clean install

# Run with H2 (development)
mvn spring-boot:run

# The API will be available at http://localhost:8002
# H2 Console: http://localhost:8002/h2-console
```

### Frontend Setup

```bash
# Navigate to frontend
cd finflow/frontend/my-app

# Install dependencies
npm install

# Start development server
npm run dev

# The app will be available at http://localhost:5173
```

### Docker Compose (Full Stack)

```bash
cd finflow/infrastructure
docker-compose up -d
```

---

## API Endpoints

### Portfolio Service (Port 8002)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/portfolio/{userId}` | Get user's portfolio |
| POST | `/api/portfolio/{userId}/holdings` | Add holding to portfolio |
| GET | `/api/portfolio/{userId}/holdings` | List all holdings |

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create new user |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user profile |
| DELETE | `/api/users/{id}` | Delete user account |
| GET | `/api/users/search?q={query}` | Search users |

---

## Repository Query Highlights

The repository layer features 150+ custom queries optimized for financial analytics:

```java
// Find all profitable portfolios
@Query("SELECT p FROM Portfolio p WHERE p.totalGainLoss > 0")
List<Portfolio> findProfitablePortfolios();

// Get holdings with unrealized gains above threshold
@Query("SELECT h FROM Holding h WHERE h.unrealizedPnL > :threshold")
List<Holding> findWinningPositions(@Param("threshold") BigDecimal threshold);

// Find accounts needing OAuth token refresh
@Query("SELECT e FROM ExternalAccount e WHERE e.tokenExpiresAt < :threshold")
List<ExternalAccount> findAccountsNeedingTokenRefresh(@Param("threshold") LocalDateTime threshold);

// Search users with case-insensitive matching
@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))")
Page<User> searchUsers(@Param("search") String search, Pageable pageable);
```

---

## Security

- **Password Hashing**: BCrypt with configurable strength
- **Stateless Authentication**: JWT tokens (handled by API Gateway)
- **Input Validation**: Jakarta Bean Validation on all DTOs
- **SQL Injection Prevention**: Parameterized queries via Spring Data JPA
- **OAuth 2.0**: Secure broker integrations with token refresh handling

---

## Roadmap

- [x] Core domain entities (User, Portfolio, Holding, Order)
- [x] Repository layer with comprehensive query methods
- [x] User service with CRUD operations
- [x] Portfolio service with holdings management
- [x] React frontend with dashboard and portfolio views
- [x] TypeScript API client with full type coverage
- [ ] API Gateway with JWT authentication
- [ ] Trading Engine for order execution
- [ ] Real-time WebSocket price updates
- [ ] Notification service (email, push, SMS)
- [ ] AI Analytics service with trade pattern recognition
- [ ] Mobile app (React Native)

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
