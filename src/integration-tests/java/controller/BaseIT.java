package controller;

import com.example.store.StoreApplication;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.TokenDTO;
import com.redis.testcontainers.RedisContainer;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StoreApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseIT {

    @LocalServerPort
    private Integer port;

    private final Long productId = 1L;
    public static String validToken = "";

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static RedisContainer redis = new RedisContainer("redis:6.2.6");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        redis.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        redis.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/store/api";
    }

    @Test
    @Order(1)
    void canCreateCustomer() {

        CustomerDTO customerDTO = given().contentType(ContentType.JSON)
                .body(CustomerControllerIT.getCustomerDTO())
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerDTO.class);
        Assertions.assertEquals(customerDTO.getName(), CustomerControllerIT.getCustomerDTO().getName());
    }

    @Test
    @Order(2)
    void canLoginSuccessfully() {
        TokenDTO tokenDTO = given().contentType(ContentType.JSON)
                .body(CustomerControllerIT.getCustomerDTO())
                .when()
                .post("/v1/customer/login")
                .then()
                .statusCode(200)
                .extract()
                .as(TokenDTO.class);
        Assertions.assertNotNull(validToken);
        validToken = tokenDTO.getToken();
    }
}
