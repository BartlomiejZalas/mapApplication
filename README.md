# Map Application #

**Author:** Bartłomiej Zalas

### Description ###

Simple application for continents, countries and cities management. 
The application utilizes SpringBoot, StringData, Postgres, Angular and Docker.

### Run Instruction ###

Clone repository:

```
git clone https://github.com/BartlomiejZalas/mapApplication.git
```

Navigate to backend directory nad build Docker image with SpringBoot service:

```
cd backend
mvn clean install dockerfile:build
```

Navigate to root directory and run docker compose to start SpringBoot service (backend), postgres DB and Angular 
application (frontend)
```
cd ..
docker-compose up
```

Open `http://localhost:4200` in your browser to see UI.

Rest service is available under `http://localhost:8080`

### Basic REST usage with cURL ###

##### Add data #####
```
curl -X POST http://localhost:8080/continents -H 'content-type: application/json' -d '{"name": "Asia"}'
curl -X POST 'http://localhost:8080/countries?continentId=1' -H 'content-type: application/json' -d '{"name": "Poland"}'
curl -X POST 'http://localhost:8080/cities?countryId=2' -H 'content-type: application/json'  -d '{"name": "Wrocław"}'
```

##### Get resource #####
```
curl -X GET http://localhost:8080/continents/1 
curl -X GET http://localhost:8080/countries/2
curl -X GET http://localhost:8080/cities/3
```

##### Query data #####
```
curl -X GET http://localhost:8080/continents
curl -X GET http://localhost:8080/countries
curl -X GET http://localhost:8080/countries?continentId=1
curl -X GET http://localhost:8080/cities
curl -X GET http://localhost:8080/cities?continentId=1
curl -X GET http://localhost:8080/cities?countryId=2
curl -X GET 'http://localhost:8080/cities?continentId=1&countryId=2'
```

##### Delete resource #####
```
curl -X DELETE http://localhost:8080/continents/1
curl -X DELETE http://localhost:8080/countries/2
curl -X DELETE http://localhost:8080/cities/3
```
