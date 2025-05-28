package controller;

import com.example.store.dto.ProductDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT extends controller.BaseIT {


    @Test
    @Order(1)
    void canCreateProduct() {

        ProductDTO productDTO =
                given()
                        .contentType(ContentType.JSON)
                        .body(getProductDTO())
                        .when()
                        .post("/product")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(ProductDTO.class);
        Assertions.assertEquals(productDTO.getName(), getProductDTO().getName());
        Assertions.assertEquals(productDTO.getDescription(), getProductDTO().getDescription());
    }

    @Test
    @Order(2)
    void canGetAllProducts() {
        List<ProductDTO> productDTOS =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/product")
                        .then()
                        .statusCode(200)
                        .body(".", hasSize(1))
                        .extract()
                        .as(new TypeRef<List<ProductDTO>>() {});
    }

    @Test
    @Order(3)
    void canGetProductById() {
        List<ProductDTO> productDTOS =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/product")
                        .then()
                        .statusCode(200)
                        .body(".", hasSize(1))
                        .extract()
                        .as(new TypeRef<List<ProductDTO>>() {});

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/"+productDTOS.get(0).getProductId())
                .then()
                .statusCode(200)
                .body("name", equalTo("product"));
    }

    public ProductDTO getProductDTO(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("product");
        productDTO.setDescription("product description");
        return productDTO;
    }

}
