package controller;

import com.example.store.StoreApplication;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.TokenDTO;
import com.redis.testcontainers.RedisContainer;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static controller.CustomerControllerIT.getCustomerDTO;
import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StoreApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIT {

    @LocalServerPort
    private Integer port;

    public static String validToken = "";
    public static String hmacEncriptorKey = "b613679a0814d9ec772f95d778c35fc5ff1697c493715653c6c712144292c5ad";

    public static String user1 = "user1";
    public static String user2 = "user2";
    public static String user3 = "user3";
    public static String user4 = "user4";
    public static String user5 = "user5";
    public static String validPassword = "P@assword1";

    public static String product1 = "product1";
    public static String product2 = "product2";
    public static String product3 = "product3";
    public static String product4 = "product4";
    public static String product5 = "product5";
    public static String product6 = "product6";
    public static String product7 = "product7";


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static RedisContainer redis = new RedisContainer("redis:6.2.6");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("token.secret", () -> hmacEncriptorKey);

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

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

    @BeforeEach
    @Test
    public void canLoginSuccessfully() {
        RestAssured.baseURI = "http://localhost:" + port + "/store/api";
        given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user1, validPassword))
                .when()
                .post("/v1/customer");
        TokenDTO tokenDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user1, validPassword))
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
