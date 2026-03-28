# 🧠 Smart Task Scheduler

An AI-powered full-stack task management system that uses Groq LLM to intelligently predict task priorities, with a human-in-the-loop override system.

## 🛠️ Technologies Used
- **Java 21** — Core backend logic
- **Spring Boot 3.2** — REST API framework
- **MySQL** — Database storage
- **Groq API (LLaMA3)** — AI priority prediction
- **HTML, CSS, JavaScript** — Frontend interface
- **Maven** — Dependency management

## ✨ Features
- Add tasks with title and description
- AI automatically predicts priority (HIGH / MEDIUM / LOW) using Groq LLM
- Manual priority override — human-in-the-loop design
- Mark tasks as completed
- Delete tasks
- Graceful fallback if AI API is unavailable

## 🗄️ Database Setup
1. Open MySQL and run the script located at `database/task_scheduler_db.sql`

## ▶️ How to Run
1. Clone the repository
2. Copy `src/main/resources/application.properties.example` to `application.properties`
3. Add your MySQL password and Groq API key
4. Run using Maven:
```bash
mvn spring-boot:run
```
5. Open browser at `http://localhost:8080`

## 🏗️ Project Structure
```
src/main/java/com/taskscheduler/
├── model/          → Task entity
├── repository/     → Database operations (JPA)
├── service/        → Business logic + Groq AI integration
└── controller/     → REST API endpoint
```

## 🔌 REST API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/tasks | Get all tasks |
| POST | /api/tasks | Create new task |
| PUT | /api/tasks/{id}/status | Update task status |
| DELETE | /api/tasks/{id} | Delete task |