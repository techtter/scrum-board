## Scrum Application

This is a simple implementation of a Scrum Board, a tool that helps visualize and manage work. Originally it was first created in Toyota automotive, but nowadays it's widely used in software development.

A Scrum Board is usually made of 3 columns - *TODO*, *InProgres*s & *Done*. In each column there are Post-it notes that represents task and their status.

As already stated this project is an implementation of such board and made of 3 separate Docker containers that holds:

- PostgreSQL database
- Java backend (Spring Boot)
- Angular frontend

The entry point for a user is a website which is available under the
address: **http://localhost:4200/**

![Scrum](https://github.com/techtter/scrum-board/blob/master/assets/scrum.gif)

---

### Prerequisites

In order to run this application you need to install two tools: **Docker** & **Docker Compose**.

Instructions how to install **Docker** on [Ubuntu](https://docs.docker.com/install/linux/docker-ce/ubuntu/), [Windows](https://docs.docker.com/docker-for-windows/install/) , [Mac](https://docs.docker.com/docker-for-mac/install/) .

**Dosker Compose** is already included in installation packs for *Windows* and *Mac*, so only Ubuntu users needs to follow [this instructions](https://docs.docker.com/compose/install/) .




### How to run it?

An entire application can be ran with a single command in a terminal:

```
$ docker-compose up -d
```

If you want to stop it use following command:

```
$ docker-compose down
```


---

#### scrum-postgres (Database)

PostgreSQL database contains only single schema with two tables - scrum
and task table.

After running the app it can be accessible using this connectors:


- Host: *localhost*
- Database: *scrum*
- User: *scrum*
- Password: *scrum*


Like other parts of application Postgres database is containerized and
the definition of its Docker container can be found in
*docker-compose.yml* file.

```yml
scrum-postgres:
    image: "postgres:9.6-alpine"
    container_name: scrum-postgres
    volumes:
      - scrum-data:/var/lib/postgresql/data
    ports:
      - 5432:5432
    environment:
      - POSTGRES_DB:scrum
      - POSTGRES_USER:scrum
      - POSTGRES_PASSWORD:scrum
```

#### scrum-app (REST API)

This is a Spring Boot (Java) based application that connects with a
database that and expose the REST endpoints that can be consumed by
frontend. It supports multiple HTTP REST methods like GET, POST, PUT and
DELETE for two resources - scrum & task.

Full list of available REST endpoints could be found in Swagger UI,
which could be called using link: **http://localhost:8080/api/swagger-ui.html**


![swagger-ui](https://github.com/techtter/scrum-board/blob/master/assets/swagger.png)


This app is also put in Docker container and its definition can be found
in a file *scrum-app/Dockerfile*. 



#### scrum-ui (Frontend)

This is a real endpoint for a user where they can manipulate their
scrums and tasks. It consumes the REST API endpoints provided by
*scrum-app*.

It can be entered using link: **http://localhost:4200/**
