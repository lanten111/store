package controller;

import com.example.store.dto.*;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderControllerIT extends BaseIT {

    @Test
    public void canGetAllOrders() {
        List<OrderDTO> orderDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/order")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .extract()
                .as(new TypeRef<List<OrderDTO>>() {});
    }

    @Test
    public void canGetProductById() {
        List<OrderDTO> orderDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/order")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .extract()
                .as(new TypeRef<List<OrderDTO>>() {});

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/order/" + orderDTOS.get(0).getOrderId())
                .then()
                .statusCode(200);
    }

    public static OrderDTO getOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        OrderCustomerDTO customerDTO = new OrderCustomerDTO();
        OrderProductDTO productDTO = new OrderProductDTO();
        customerDTO.setCustomerId(customerId);
        productDTO.setProductId(productId);
        orderDTO.setCustomer(customerDTO);
        orderDTO.setProducts(List.of(productDTO));
        return orderDTO;
    }

}
