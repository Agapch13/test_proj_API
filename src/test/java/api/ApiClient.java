package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.request.EntityRequest;
import models.response.EntityResponse;
import org.apache.http.HttpStatus;
import utils.JsonUtils;

import java.util.List;

public class ApiClient {

    private final static String BASE_URI = "http://localhost:8080";

    private final RequestSpecification baseSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    public ValidatableResponse create(EntityRequest entityRequest) {
        String requestBody = JsonUtils.pojoToJson(entityRequest);

        return RestAssured.given()
                .spec(baseSpec)
                .basePath("/api/create/")
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    public List<EntityResponse> getAll() {
        return RestAssured.given()
                .spec(baseSpec)
                .get("/api/getAll")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList("entity", EntityResponse.class);
    }

    public EntityResponse getById(int id) {
        ValidatableResponse validatableResponse = getById(id, HttpStatus.SC_OK);
        return validatableResponse.extract()
                .body()
                .as(EntityResponse.class);
    }

    public ValidatableResponse getById(int id, int statusCode) {
        return RestAssured.given()
                .spec(baseSpec)
                .pathParam("id", id)
                .get("/api/get/{id}")
                .then()
                .statusCode(statusCode);

    }

    public void deleteById(int id, int statusCode) {
        ValidatableResponse validatableResponse = RestAssured.given()
                .spec(baseSpec)
                .pathParam("id", id)
                .delete("/api/delete/{id}")
                .then()
                .statusCode(statusCode);

        System.out.println();
    }

    public void patch(int id, EntityRequest updatedEntity) {
        String requestBody = JsonUtils.pojoToJson(updatedEntity);

        ValidatableResponse validatableResponse = RestAssured.given()
                .spec(baseSpec)
                .body(requestBody)
                .when()
                .pathParam("id", id)
                .patch("/api/patch/{id}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        System.out.println();
    }
}
