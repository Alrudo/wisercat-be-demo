# Wisercat-be-demo Service
This project is built as a demo application for Wisercat by Aleksandr Rudoi.

## Prerequisites
- Java 17
- Docker

## Running guide
Execute the [docker-compose.yml](docker-compose.yml) by either running the green arrow or executing console
command from the root folder:
```bash
docker-compose up -d
```

Make sure that ports `5432` for PostgreSQL database, `5050` for pgadmin and `8080` for the service itself.

PostgreSQL and pgadmin ports can be changed in [docker-compose.yml](docker-compose.yml), e.g.:
```yaml
ports:
  "{desired_port}:5432"
```

Application port can be changed in [application.yml](src/main/resources/application.yml):
```yaml
server:
  port: {desired_port}
```

## Testing
Tests use testcontainers for acceptance testing thus requiring Docker
for their execution.