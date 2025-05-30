package controller;

import com.example.store.dto.ProductDTO;

import org.junit.jupiter.api.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProductControllerIT extends BaseIT {

    @Test
    public void canCreateProduct() {
        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product1))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);
        Assertions.assertEquals(productDTO.getName(), product1);
        Assertions.assertEquals(productDTO.getDescription(), product1 + "description");
    }

    @Test
    public void canGetAllProducts() {

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product6))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);
        List<ProductDTO> productDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/product")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<ProductDTO>>() {});
        Assertions.assertNotEquals(0, productDTOS.size());
    }

    @Test
    public void canGetProductById() {
        ProductDTO productDTO = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .body(getProductDTO(product7))
                .when()
                .post("/v1/product")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + BaseIT.validToken)
                .when()
                .get("/v1/product/" + productDTO.getProductId())
                .then()
                .statusCode(200)
                .body("name", equalTo(product7));
    }

    public static ProductDTO getProductDTO(String name) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(name + "description");
        return productDTO;
    }
}
