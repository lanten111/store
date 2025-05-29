package controller;

import com.example.store.dto.CustomerDTO;

import org.junit.jupiter.api.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CustomerControllerIT extends BaseIT {

    @Test
    public void canGetAllCustomers() {
        List<CustomerDTO> customerDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/customer")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .extract()
                .as(new TypeRef<List<CustomerDTO>>() {});
    }

    @Test
    public void canSearchCustomersByName() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/customer/search/j")
                .then()
                .statusCode(200)
                .body("name", contains(getCustomerDTO().getName()));
    }

    // can add tests to test exception

    public static CustomerDTO getCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John doe");
        customerDTO.setEmail("john@does.com");
        customerDTO.setPassword("P@assweord123");
        return customerDTO;
    }
}
