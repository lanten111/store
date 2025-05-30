package controller;

import com.example.store.dto.CustomerDTO;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CustomerControllerIT extends BaseIT {

    @Test
    public void canCreateCustomer() {
        CustomerDTO customerDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user2, validPassword))
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerDTO.class);
        Assertions.assertEquals(customerDTO.getName(), user2);
    }


    @Test
    public void canGetAllCustomers() {
        given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user4, validPassword))
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201);
        List<CustomerDTO> customerDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/customer")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<CustomerDTO>>() {});
        Assertions.assertNotEquals(0, customerDTOS.size());
    }

    @Test
    public void canSearchCustomersByName() {
        String searchUser = "x1User";
        String searchString = "x1";
        given().contentType(ContentType.JSON)
                .body(getCustomerDTO(searchUser, validPassword))
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201);
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/customer/search/"+searchString)
                .then()
                .statusCode(200)
                .body("name", contains(searchUser));
    }

    public void canLogout() {
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .post("/v1/customer/logout")
                .then()
                .statusCode(200)
                .extract()
                .response();
        Assertions.assertNotNull(response.toString());
    }

    // can add tests to test exception

    public static CustomerDTO getCustomerDTO(String user, String password) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(user);
        customerDTO.setEmail(user + "@mail.com");
        customerDTO.setPassword(password);
        return customerDTO;
    }
}
