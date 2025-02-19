
# **GitHub Activity Tracker with Redis Caching**

This Spring Boot application tracks the activities of a specific GitHub user and caches the data using Redis. Redis is used to enhance performance and scalability by storing frequently accessed data.

---

## **Table of Contents**
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [Configuration](#configuration)
- [Endpoints](#endpoints)
- [Project Structure](#project-structure)
- [Example Request](#example-request)
- [Caching Strategy](#caching-strategy)
- [Error Handling](#error-handling)
- [Future Improvements](#future-improvements)
- [License](#license)
- [Contribution](#contribution)
- [Acknowledgments](#acknowledgments)
- [Contact](#contact)

---

## **Features**
- Fetches recent activity of a GitHub user from the GitHub API.
- Displays activity details, including:
    - Event type (e.g., PushEvent, IssuesEvent, WatchEvent)
    - Repository name
    - Event timestamp
- Supports filtering by event type using a query parameter.
- Caches the activity data using Redis to improve performance.
- Uses WebClient for non-blocking API calls.
- DTO (Data Transfer Object) pattern for cleaner and more maintainable code.
- Handles errors gracefully with meaningful messages.

---

## **Technologies Used**
- **Java 21**
- **Spring Boot**
- **Spring WebFlux** (for non-blocking HTTP requests)
- **Redis** (for caching)
- **Spring Cache** (with Redis integration)
- **WebClient** (for making API calls to GitHub)
- **Lombok** (for reducing boilerplate code)
- **Maven** (for dependency management)

---

## **Getting Started**

### **Prerequisites**
- Java 21 or later
- Maven 3.8 or later
- Docker (for running Redis)

### **Installation**
1. **Clone the repository:**  
   ```sh
   git clone https://github.com/bdkamaci/github-activity.git
   cd github-activity
   ```

2. **Start Redis using Docker:**  
   ```sh
   docker run --name redis-cache -p 6379:6379 -d redis
   ```

3. **Install dependencies:**  
   ```sh
   mvn clean install
   ```

### **Running the Application**
```sh
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

---

## **Configuration**
Update the `application.properties` file with the following configuration:  
```properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379

github.api.url=https://api.github.com/users/
```

---

## **Endpoints**
- **Get User Activities:**  
  ```http
  GET /api/v1/activity/{username}
  ```
    - Fetches all activities of the specified GitHub user.

- **Get User Activities by Event Type:**  
  ```http
  GET /api/v1/activity/{username}?eventType={eventType}
  ```
    - Fetches activities filtered by event type (e.g., PushEvent, IssueEvent).

---

## **Project Structure**
```css
src/main/java/com.bdkamaci.githubactivity
│
├── controller
│   └── GithubActivityController.java      # REST controller for GitHub activity
│
├── service
│   ├── GitHubActivityService.java          # Interface for service layer
│   └── impl
│       └── GitHubActivityServiceImpl.java  # Implementation of the service
│
├── exception
│
├── config
│
├── client
│   └── GitHubClient.java                  # WebClient for GitHub API requests
│
├── dto
│   └── ActivityDTO.java                   # Data Transfer Object for activity response
│
└── model
    ├── Actor.java                         # Model for actor details
    ├── Commit.java                        # Model for commit details
    ├── CommitAuthor.java                   # Model for commit author details
    ├── GitHubEvent.java                    # Model for GitHub event details
    ├── Payload.java                       # Model for event payload
    └── Repo.java                          # Model for repository details
```

---

## **Example Request**
```http
GET http://localhost:8080/api/v1/activity/octocat?eventType=PushEvent
```

**Response:**  
```json
[
{
"eventType": "PushEvent",
"repoName": "octocat/Hello-World",
"createdAt": "2025-02-18T14:19:25Z"
}
]
```

---

## **Caching Strategy**
- Redis is used as the caching layer for storing user activities.
- Cache Key Pattern: `activityCache::{username}-{eventType}`
- Cache Expiration (TTL) is configured in `application.properties`:  
  ```properties
  spring.cache.redis.time-to-live=600000  # 10 minutes
  ```
- If a request is made within the TTL period, cached data is returned. Otherwise, data is fetched from the GitHub API.

---

## **Error Handling**
- `UserNotFoundException`: Thrown when an invalid GitHub username is provided.
- `RuntimeException`: Thrown when an error occurs on the GitHub API server.
- Custom error messages are returned in a consistent JSON format.

---

## **Future Improvements**
- Implement pagination for large activity lists.
- Enhance caching strategy for better performance.
- Add more detailed logging and monitoring.
- Implement unit and integration tests for all layers.

---

## **License**
This project is licensed under the MIT License.

---

## **Contribution**
Contributions are welcome! Feel free to open an issue or submit a pull request.

---

## **Acknowledgments**

- Spring Boot Documentation
- Redis Documentation
- ObjectMapper Library
- https://roadmap.sh/projects/github-user-activity

---

## **Contact**

Created by Burcu Doga KAMACI - feel free to contact me!

---