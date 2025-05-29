package controller;

import com.example.store.dto.ProductDTO;

import org.junit.jupiter.api.*;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class ProductControllerIT extends BaseIT {

    @Test
    public void canGetAllProducts() {
        List<ProductDTO> productDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/product")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .extract()
                .as(new TypeRef<List<ProductDTO>>() {});
    }

    @Test
    public void canGetProductById() {
        List<ProductDTO> productDTOS = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/product")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .extract()
                .as(new TypeRef<List<ProductDTO>>() {});

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+BaseIT.validToken)
                .when()
                .get("/v1/product/" + productDTOS.get(0).getProductId())
                .then()
                .statusCode(200)
                .body("name", equalTo("product"));
    }

    public static ProductDTO getProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("product");
        productDTO.setDescription("product description");
        return productDTO;
    }
}
