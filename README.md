

# Application Update

## Feature that i could not get to due to time
  * Update of openAI documentation
  * Unit test for Order service
  * Admin roles that can get all customer and create products
  * More unit tests to cover exceptions and validations
  * More integration tests to cover exception handling
  * Adding/update test data generator

## FYI
 * Disable test data migration, as it needs update
 * Removed existing tests, as they were combining unit and integration tests

## 1. Application YML settings
- **Database**:
   - Connects to a PostgreSQL database using environment variables:
      - `DB_HOST`, `DB_USERNAME`, and `DB_PASSWORD`.
   - Example: `jdbc:postgresql://${DB_HOST}:5432/store`.
- **Redis**:
   - Defines the Redis host (`REDIS_HOST`) and port (`6379`).

## 2. Cache Configuration
- **TTL (Time-to-Live)**:
   - Specifies expiration times (in seconds) for different caches.
   - Example: `CUSTOMER_LIST_DTO_CACHE` and `PRODUCT_DTO_CACHE` expire after 30 seconds.


## Pipeline
  * The project has a pipeline , tha runs unit tests, then integration tests, then build the image
  * The image is then pushed to dockerhub and publicly available at https://hub.docker.com/r/mumakahdo/store

# Running the Application

 * A postman collection is included at `.docker/store.json` make sure env exist with {host} as "http://localhost:8086/store/api/v1" and {token} which comes from login call
 * Endpoint to create a customer and to login are open

This guide explains how to run the application using Docker. You can either **use the prebuilt Docker image** or **build the image yourself**.

## 1. Using Prebuilt Docker Image

### Prerequisites
- Ensure Docker is installed on your machine.

### Steps
1. **Access the Prebuilt Docker Image**
   - A prebuilt image is available at: [Docker Hub - mumakahdo/store](https://hub.docker.com/r/mumakahdo/store).

2. **Obtain the Docker Compose File**
   - You can use the `docker-compose.yml` file:
      - Located at `.docker/docker-compose.yml` in the project.
      - Or download it from GitHub:  
        [GitHub - store docker-compose.yml](https://github.com/lanten111/store/blob/main/.docker/docker-compose.yml).

3. **Start Services**
   - The `docker-compose.yml` file defines three services:
      - **Store App**: The main application container.
      - **Database (DB)**: A containerized database instance.
      - **Redis**: A containerized Redis service.
   - To run the services, execute the following command in the directory containing the `docker-compose.yml` file:
     ```bash
     docker compose -f docker-compose.yml up -d
     ```

4. **Secrets Configuration**
   - The `docker-compose.yml` file includes prepopulated secrets. You can modify them if needed, but ensure the updated secrets are **consistent across all services**.

---

## 2. Building the Docker Image Yourself

If you prefer to build the Docker image locally instead of using the prebuilt one, follow these steps:

### Prerequisites
- Ensure:
   - Docker is installed.
   - The source code is available locally.
   - The `Dockerfile` is located in the project directory (typically in the root).

### Steps
1. **Navigate to the Project Directory**
   - Open your terminal and navigate to the root of the project where the `Dockerfile` is located:
   - e.g
     ```bash
     cd /path/to/your/project/store
     ```

2. **Build the Docker Image**
   - Run the following command to build the image:
     ```bash
     docker build -t custom-store:latest .
     ```
   - This command:
      - Uses the `Dockerfile` in the current directory.
      - Tags the image as `custom-store` with the `latest` tag.

3. **Update the Docker Compose File**
   - Modify the `docker-compose.yml` file to use your locally built image. Replace the prebuilt image line (e.g., `image: mumakahdo/store`) with:
     ```yaml
     image: custom-store:latest
     ```

4. **Start Services**
   - Use the updated Docker Compose file to spin up the services:
     ```bash
     docker compose -f docker-compose.yml up -d
     ```

5. **Secrets Configuration**
   - As with the prebuilt image, the `docker-compose.yml` file includes predefined secrets. If you modify the secrets, ensure they match across all your services.

## 3. Running the Application Natively with `gradlew.bat`

If you already have the database and Redis services running, you can run the application locally without Docker by injecting the required environment variables.

### Prerequisites
- Ensure:
   - **Java 17** (or the required version) is installed on your machine.
   - **Gradle Wrapper** (`gradlew.bat`) is available in the project directory.
   - The database and Redis services are running and properly configured.

### Steps
1. **Set the Required Environment Variables**
   - Before starting the application, set the necessary environment variables. These include the database, Redis, security token secret, **and Java location**.
   - For **Windows**, you can set them in the terminal using:
     ```bash
     set DB_USERNAME=dbusername
     set DB_PASSWORD=dbpassword
     set DB_HOST=dbhost
     set REDIS_HOST=dbhosat
     set TOKEN_SECRET=IS5IobIItq0pRX9JL9TvdQ40Oa93u2Wojlign4V3L30
     set JAVA_HOME=C:\Path\To\Java\jdk-17
     ```
   - Replace `C:\Path\To\Java\jdk-17` with the actual path to your Java installation (e.g., `C:\Program Files\Java\jdk-17`).
   - Replace password, username to your db and redis instance

   - To ensure environment variables are set correctly, you can verify them using:
     ```bash
     echo %DB_USERNAME%
     echo %JAVA_HOME%
     ```

2. **Navigate to the Project Directory**
   - Open your terminal and change your directory to the root of the project where the `gradlew.bat` file is located:
     ```bash
     cd /path/to/your/project/store
     ```

3. **Run the Application**
   - Execute the following command to start the application:
     ```bash
     gradlew.bat bootRun
     ```
   - This command uses Spring Boot’s `bootRun` task to launch the application.

4. **Configuration**
   - Ensure your database and Redis instances match the connection configurations provided in the environment variables.
   - The application settings in `application.yml` rely on the injected environment variables for connecting to external services.

5. **Access the Application**
   - Once the application starts, it should be accessible at the configured endpoint (e.g., `http://localhost:8080` by default).



* ------------------------------------------------------------------------------------------------------------------------



# Store Application
The Store application keeps track of customers and orders in a database.

# Assumptions
This README assumes you're using a posix environment. It's possible to run this on Windows as well:
* Instead of `./gradlew` use `gradlew.bat`
* The syntax for creating the Docker container is different. You could also install PostgreSQL on bare metal if you prefer


# Prerequisites
This service assumes the presence of a postgresql 16.2 database server running on localhost:5433 (note the non-standard port)
It assumes a username and password `admin:admin` can be used.
It assumes there's already a database called `store`

You can start the PostgreSQL instance like this:
```shell
docker run -d \
  --name postgres \
  --restart always \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=store \
  -v postgres:/var/lib/postgresql/data \
  -p 5433:5432 \
  postgres:16.2 \
  postgres -c wal_level=logical
```

# Running the application
You should be able to run the service using
```shell
./gradlew bootRun
```

The application uses Liquibase to migrate the schema. Some sample data is provided. You can create more data by reading the documentation in utils/README.md

# Data model
An order has an ID, a description, and is associated with the customer which made the order.
A customer has an ID, a name, and 0 or more orders.

# API
Two endpoints are provided:
* /order
* /customer

Each of them supports a POST and a GET. The data model is circular - a customer owns a number of orders, and that order necessarily refers back to the customer which owns it.
To avoid loops in the serializer, when writing out a Customer or an Order, they're mapped to CustomerDTO and OrderDTO which contain truncated versions of the dependent object - CustomerOrderDTO and OrderCustomerDTO respectively.

The API is documented in the OpenAPI file OpenAPI.yaml. Note that this spec includes part of one of the tasks below (the new /products endpoint)

# Tasks

1. Extend the order endpoint to find a specific order, by ID
2. Extend the customer endpoint to find customers based on a query string to match a substring of one of the words in their name
3. Users have complained that in production the GET endpoints can get very slow. The database is unfortunately not co-located with the application server, and there's high latency between the two. Identify if there are any optimisations that can improve performance
4. Add a new endpoint /products to model products which appear in an order:
   * A single order contains 1 or more products.
   * A product has an ID and a description.
   * Add a POST endpoint to create a product
   * Add a GET endpoint to return all products, and a specific product by ID
      * In both cases, also return a list of the order IDs which contain those products
   * Change the orders endpoint to return a list of products contained in the order

# Bonus points
1. Implement a CI pipeline on the platform of your choice to build the project and deliver it as a Dockerized image

# Notes on the tasks
Assume that the project represents a production application.
Think carefully about the impact on performance when implementing your changes
The specifications of the tasks have been left deliberately vague. You will be required to exercise judgement about what to deliver - in a real world environment, you would clarify these points in refinement, but since this is a project to be completed without interaction, feel free to make assumptions - but be prepared to defend them when asked.
There's no CI pipeline associated with this project, but in reality there would be. Consider the things that you would expect that pipeline to verify before allowing your code to be promoted
Feel free to refactor the codebase if necessary. Bad choices were deliberately made when creating this project.
