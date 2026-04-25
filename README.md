# TopTech Bot

A backend Telegram bot built with Spring Boot that delivers real-time trending tech stories from Hacker News to users via chat commands.

---

## Overview

TopTech Bot is a stateless backend service that integrates with the Hacker News public API and the Telegram Bot API. Users interact through Telegram commands and receive formatted news stories on demand. There is no frontend, no database, and no user state persisted between sessions.

---

## Features

- Returns the top 10 trending Hacker News stories on command
- Each story includes: title, URL, and score
- Handles Telegram's message length limit via response chunking
- Runs as a long-polling bot — no webhook configuration required
- Containerized with Docker for consistent builds and deployment

---

## Tech Stack

| Component        | Technology                          |
|-----------------|-------------------------------------|
| Language         | Java 17                             |
| Framework        | Spring Boot                         |
| Bot Integration  | Telegram Bots Java API (Long Poll)  |
| HTTP Client      | RestTemplate                        |
| External API     | Hacker News Firebase REST API       |
| Build Tool       | Maven                               |
| Containerization | Docker                              |
| Hosting          | Render                              |

---

## Architecture

The application has three logical layers:

```
Telegram Client
      │
      ▼
Bot Handler Layer
  └── Receives user commands via long polling
      │
      ▼
Service Layer
  └── Calls Hacker News API
      ├── GET /topstories.json       → returns list of top story IDs
      └── GET /item/{id}.json        → returns details per story
      │
      ▼
Response Formatter
  └── Formats stories into text blocks
  └── Chunks output if response exceeds Telegram's message limit
      │
      ▼
Telegram Client (response delivered)
```

Bot token and username are supplied via environment variables and are not hardcoded.

---

## Bot Commands

| Command  | Description                          |
|---------|--------------------------------------|
| `/start` | Sends a welcome message              |
| `/fetch` | Returns the top 10 trending stories  |
| `/help`  | Lists available commands             |

**Example `/fetch` output:**

```
1. I built an open-source alternative to Datadog
   https://github.com/...
   Score: 427

2. Ask HN: What's your current stack in 2025?
   https://news.ycombinator.com/item?id=...
   Score: 389
```

---

## Local Setup

### Prerequisites

- Java 17+
- Maven 3.8+
- A Telegram bot token from @BotFather

### Steps

**1. Clone the repository**

```bash
git clone https://github.com/your-username/toptech-bot.git
cd toptech-bot
```

**2. Set credentials**

In `src/main/resources/application.properties`:

```properties
telegram.bot.username=YOUR_BOT_USERNAME
telegram.bot.token=YOUR_BOT_TOKEN
```

Do not commit credentials. Use environment variables or a secrets manager in any non-local environment.

**3. Run the application**

```bash
mvn spring-boot:run
```

---

## Docker

**Build:**

```bash
docker build -t toptech-bot .
```

**Run:**

```bash
docker run \
  -e TELEGRAM_BOT_USERNAME=your_username \
  -e TELEGRAM_BOT_TOKEN=your_token \
  toptech-bot
```

---

## Deployment

The application is deployed on Render as a Docker container. It runs continuously and responds to Telegram messages via long polling.

**Note:** On Render's free tier, the service spins down after a period of inactivity. The first response after an idle period may be delayed by several seconds while the instance restarts.

---

## Limitations

- **No persistence** — no user data, preferences, or history is stored
- **No authentication** — the bot responds to any Telegram user who discovers it
- **No rate limiting** — all incoming commands are processed without throttling
- **Sequential API calls** — story details are fetched one at a time; not parallelized
- **Free-tier cold starts** — Render free instances idle and restart on traffic

---

## Possible Improvements

- Parallel story fetching to reduce `/fetch` response time
- `/latest` command to return newest stories instead of top-ranked
- Scheduled daily digest pushed to subscribed users
- Rate limiting per user to prevent command flooding
- Support for additional sources (e.g., Reddit, Dev.to)

---

## Author

**Madhu Maneesh**  
[GitHub](https://github.com/madhu-maneesh) · [LinkedIn]({https://www.linkedin.com/in/shetty-madhu-maneesh)
