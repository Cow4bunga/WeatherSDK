# Weather API Application

## Overview
This Weather API Application provides weather forecasts for cities using data from the OpenWeatherMap API. The application is built with Spring Boot and offers a RESTful interface for interacting with weather data.

## Getting Started

### Prerequisites
- Docker
- Docker Compose
- An active OpenWeatherMap API key (to be provided as a parameter)

### Running the Application
To run the application, navigate to the root directory of the project and execute the following command:
```bash
docker-compose up
```

### Accessing Swagger Documentation
Once the application is running, you can access the Swagger API documentation at:
```
http://localhost:8084/api-docs
```

To view the interactive API documentation UI, visit:
```
http://localhost:8084/swagger-ui/index.html
```

### Endpoints
- **Create SDK**: `POST /sdk/{apiKey}/{mode}`
- **Get Forecast**: `GET /sdk/{apiKey}/forecast/{cityName}`
- **Delete SDK**: `DELETE /sdk/{apiKey}`

## Sequence Diagram
Below is the sequence diagram for the SDK interactions.

Below is the sequence diagram for the SDK interactions.

![Sequence Diagram](SDK.png)

## Testing
Unit and integration tests are located in the `test/java` directory. You can run the tests using:
```bash
mvn test
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgements
- [OpenWeatherMap API] for weather data.
- [Spring Boot] for building the application.