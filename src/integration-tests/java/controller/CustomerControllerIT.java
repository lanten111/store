package controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.ProductDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIT extends BaseIT {


    @Test
    @Order(1)
    void canCreateCustomer() {

        CustomerDTO customerDTO =
                given()
                        .contentType(ContentType.JSON)
                        .body(getCustomerDTO())
                        .when()
                        .post("/customer")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(CustomerDTO.class);
        Assertions.assertEquals(customerDTO.getName(), getCustomerDTO().getName());
    }

    @Test
    @Order(2)
    void canGetAllCustomers() {
        List<CustomerDTO> customerDTOS =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/customer")
                        .then()
                        .statusCode(200)
                        .body(".", hasSize(1))
                        .extract()
                        .as(new TypeRef<List<CustomerDTO>>() {});
    }

    @Test
    @Order(3)
    void canSearchCustomersByName() {
//        List<ProductDTO> productDTOS =
//                given()
//                        .contentType(ContentType.JSON)
//                        .when()
//                        .get("/product")
//                        .then()
//                        .statusCode(200)
//                        .body(".", hasSize(1))
//                        .extract()
//                        .as(new TypeRef<List<ProductDTO>>() {});

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/customer/search/j")
                .then()
                .statusCode(200)
                .body("name", contains(getCustomerDTO().getName()));
    }

    public CustomerDTO getCustomerDTO(){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John doe");
        return customerDTO;
    }

}
