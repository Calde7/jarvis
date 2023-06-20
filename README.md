<h1 align="center"> JARVIS </h1>

# Java API REST Versioned Interactive System

<h4 align="center">
:construction: Project WIP :construction:
</h4>

## :hammer: Project functionalities

- `Endpoint /ping`:
  ```
  curl --location 'http://localhost:8080/v1/ping'
  ``` 
- `Service calculate`: Service that receives 2 numbers, adds them, and applies a percentage increase that must be purchased from an external service
  ```
  curl --location 'http://localhost:8080/v1/calculate/{{number1}}/{{number2}}'
  ```

- `Service Tracking History`: History of all calls to all endpoints
    ```
    curl --location 'http://localhost:8080/v1/tracking?offset=0&pageSize=10'
    ```

# Step to run the project

### 1) Open a terminal and go to the root of the project.
### 2) Run command `mvn clean install`. This will create file `.jar`
### 3) Run command `docker-compose up`. This generate two images with containers

    CONTAINER ID   IMAGE             COMMAND
    5eab7c9de729   jarvis:latest     "java -jar /jarvis.j…"
    8dec0229291f   postgres:latest   "docker-entrypoint.s…"

# Postman Collections

``` src/main/resources/Jarvis.postman_collection.json ```

# References

- ### Rate Limiting a Spring API Using Bucket4j
  See [Bucket4j](https://www.baeldung.com/spring-bucket4j) for documentation and more information.

- ### Guava Cache
  See [Guava](https://www.baeldung.com/guava-cache) for documentation and more information.

- ### CSRNG (Random Number API)
  #### CSRNG Lite random number generator API
  See [CSRNG](https://csrng.net/) for documentation and more information.


