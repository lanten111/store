package controller;

import com.example.store.dto.*;

import org.junit.jupiter.api.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static controller.CustomerControllerIT.getCustomerDTO;
import static controller.ProductControllerIT.getProductDTO;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderControllerIT extends BaseIT {

    @Test
    public void canCreateOrder() {

        CustomerDTO customerDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user2, validPassword))
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerDTO.class);

        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product2))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);

        OrderDTO orderDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getOrderDTO(customerDTO.getCustomerId(), productDTO.getProductId()))
                .when()
                .post("/v1/order")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderDTO.class);
        Assertions.assertNotNull(orderDTO.getOrderId());
    }

    @Test
    public void canGetAllOrders() {
        CustomerDTO customerDTO = given().contentType(ContentType.JSON)
                .body(getCustomerDTO(user5, validPassword))
                .when()
                .post("/v1/customer")
                .then()
                .statusCode(201)
                .extract()
                .as(CustomerDTO.class);

        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product5))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);

        OrderDTO orderDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getOrderDTO(customerDTO.getCustomerId(), productDTO.getProductId()))
                .when()
                .post("/v1/order")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderDTO.class);
        List<OrderDTO> orderDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/order")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<OrderDTO>>() {});
        Assertions.assertNotEquals(0, orderDTOS.size());
    }

    @Test
    public void canGetProductById() {

        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product4))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);

        ProductDTO newProductDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/product/" + productDTO.getProductId())
                .then()
                .statusCode(200)
                .extract()
                .as(ProductDTO.class);
        Assertions.assertEquals(product4, newProductDTO.getName());
    }

    public static OrderDTO getOrderDTO(Long customerId, Long productId) {
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
