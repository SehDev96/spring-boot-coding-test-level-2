# Getting Started

---
### Steps to run application  
1. Clone the repository </br>
   <em>$ git clone https://github.com/SehDev96/spring-boot-coding-test-level-2.git</em>
   
2. Change directory and run docker compose to spin up postgres container. </br>
   <em>$ cd spring-boot-coding-test-level-2 && docker-compose -f docker/docker-compose.yml up -d</em>
   
3. Run the application and the server should be running at port 8080

---

Application Setup (DO THIS FIRST)

1. Fork this repository into your GitHub account (You can create a GitHub account if you don't have one)
2. Clone the repository from your repository.
3. Run the project, ensure project run without any problem.

### Project Overview
This project is about a backend of project management application. User can create a project and practice agile methodology using SCRUM framework.

A project will be able to have team members and tasks which can be assigned to **ONLY** one team member per task.

Team member only have visibility to their projects.

### Instructions (MANDATORY) - FOLLOW EXACTLY
1. Create a new branch name `feature/rest-api`
2. Create entities (you can add more entities and more fields inside the entity should you need it)
   1. User
      1. id | uuid | required | pk
      2. username | string | required | unique
      3. password | string | required 
   2. Project
      1. id | uuid | required | pk
      2. name | string | required | unique
   3. Task
      1. id | uuid | required | pk
      2. title | string | required
      3. description | string
      4. status | string | required
      5. project_id | uuid | required
      6. user_id | uuid | required
3. Commit the changes with message "Create entities"
4. Create REST APIs to manage User, Project and Task, your APIs must include
   1. GET retrieve all resources (ex. `GET /api/v1/users`)
   2. GET retrieve one resource by id (ex. `GET /api/v1/users/{user_id}`)
   3. POST create one resource (ex. `POST /api/v1/users`)
   4. PUT update one resource idempotent (ex. `PUT /api/v1/users/{user_id}`)
   5. PATCH update one resource (ex. `PATCH /api/v1/users/{user_id}`)
   6. DELETE remove one resource (ex. `DELETE /api/v1/users/{user_id}`)
5. Add any other APIs and codes that you think you might need in order for this app to work properly
6. Commit the changes with message "Create REST APIs"
7. Create a PR against `main` branch and merge it, don't forget to delete the feature branch afterwards
8. Create a new branch name `feature/core-functions` from `main`
9. Do these features:
   1. Only `ADMIN` role can use User API
   2. Only `PRODUCT_OWNER` role can create a project and tasks
   3. Only `PRODUCT_OWNER` role can assign tasks to a team member in their project
   4. Task will have either `NOT_STARTED`, `IN_PROGRESS`, `READY_FOR_TEST`, `COMPLETED` status
   5. When a task is created, status will be `NOT_STARTED`
   6. Team member **can only** change the status of the task assigned to them, they can edit any other attribute in a task.
10. Commit the changes with message "Implement features"
11. Write tests for:
    1. Creating a user, use `TestRestTemplate`
    2. Creating a project and assign 2 users to it.
    3. User change the status of a task assigned to themselves
12. Commit the changes with message "Write test"
13. Implement pagination feature `GET /api/v1/projects` API. It should be able to receive query strings:
    1. `q` -> search keyword, will search for name
    2. `pageIndex` -> the index of the page to shown, default `0`
    3. `pageSize` -> how many items to return, default `3`
    4. `sortBy` -> attribute to sort, default `name`
    5. `sortDirection` -> direction of the sort, default `ASC`
14. Commit the changes with message "Implement pagination"
15. Create a postman collection for every API that you have created along with examples and put it in `docs` directory
16. Commit the changes with message "Add postman collection"
17. Create a PR against `main` branch and merge it, don't forget to delete the feature branch afterwards

### Instructions (BONUS)
1. Implement caching for the pagination API using `ConcurrentMap` or `redis`
2. Package the application into a Docker image and deploy it to Heroku
3. Implement distributed tracing with sleuth
