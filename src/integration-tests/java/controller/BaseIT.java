package controller;

import com.example.store.StoreApplication;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.dto.TokenDTO;
import com.redis.testcontainers.RedisContainer;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static controller.CustomerControllerIT.getCustomerDTO;
import static controller.OrderControllerIT.getOrderDTO;
import static controller.ProductControllerIT.getProductDTO;
import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StoreApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseIT {

    @LocalServerPort
    private Integer port;

    public static String validToken = "";
    public static Long customerId;
    public static Long productId;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static RedisContainer redis = new RedisContainer("redis:6.2.6");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
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
        canLogout();
        postgres.stop();
        redis.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/store/api";
    }

    @Test
    @Order(1)
    public void canCreateCustomer() {

        CustomerDTO customerDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO())
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerDTO.class);
        Assertions.assertEquals(customerDTO.getName(), getCustomerDTO().getName());
        customerId = customerDTO.getCustomerId();
    }

    @Test
    @Order(2)
    public void canLoginSuccessfully() {

        TokenDTO tokenDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO())
                .when()
                .post("/v1/customer/login")
                .then()
                .statusCode(200)
                .extract()
                .as(TokenDTO.class);
        Assertions.assertNotNull(validToken);
        validToken = tokenDTO.getToken();
    }

    @Test
    @Order(3)
    public void canCreateProduct() {
        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO())
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);
        Assertions.assertEquals(productDTO.getName(), getProductDTO().getName());
        Assertions.assertEquals(productDTO.getDescription(), getProductDTO().getDescription());
        productId = productDTO.getProductId();
    }

    @Test
    public void canCreateOrder() {

        OrderDTO orderDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getOrderDTO())
                .when()
                .post("/v1/order")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderDTO.class);
        Assertions.assertNotNull(orderDTO.getOrderId());
    }

    public static void canLogout() {
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getCustomerDTO())
                .when()
                .post("/v1/customer/logout")
                .then()
                .statusCode(200)
                .extract()
                .response();
        Assertions.assertNotNull(response.toString());
    }
}
