package tests;

import api.ApiClient;
import io.restassured.response.ValidatableResponse;
import models.request.EntityRequest;
import models.request.EntityRequest.Addition;
import models.response.EntityResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestTest {

    final ApiClient apiClient = new ApiClient();

    @Test
    void createTest() {
        EntityRequest entityRequest = new EntityRequest(
                new Addition("Info", 123),
                List.of(1, 2, 3, 4, 5),
                "Title",
                true);

        ValidatableResponse response = apiClient.create(entityRequest);
        String createdEntityId = response.extract()
                .body()
                .asString();
        assertNotNull(createdEntityId, "Id of the created entity should not be null");
    }

    @Test
    void getByIdTest() {
        EntityRequest entityRequest = new EntityRequest(
                new Addition("Info", 123),
                List.of(1, 2, 3, 4, 5),
                "Title",
                true);

        ValidatableResponse response = apiClient.create(entityRequest);
        int createdEntityId = Integer.parseInt(response.extract()
                .body()
                .asString());

        EntityResponse expectedResponse = EntityResponse.create(createdEntityId, entityRequest);
        EntityResponse actualResponse = apiClient.getById(createdEntityId);
        assertEquals(expectedResponse, actualResponse, "");
    }

    @Test
    void getAllTest() {
        List<EntityResponse> all = apiClient.getAll();
        assertTrue(!all.isEmpty(), "Response list should not be empty");
    }

    @Test
    void deleteByIdTest() {
        EntityRequest entityRequest = new EntityRequest(
                new Addition("Info", 123),
                List.of(1, 2, 3, 4, 5),
                "Title",
                true);

        ValidatableResponse response = apiClient.create(entityRequest);
        int createdEntityId = Integer.parseInt(response.extract()
                .body()
                .asString());

        apiClient.deleteById(createdEntityId, HttpStatus.SC_NO_CONTENT);

        ValidatableResponse errorResponse = apiClient.getById(createdEntityId, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        errorResponse.body("error", Matchers.equalTo("no rows in result set"));
    }

    @Test
    void patchTest() {
        EntityRequest initialEntity = new EntityRequest(
                new Addition("Info", 1234),
                List.of(1, 2, 3, 4, 5),
                "Title",
                false);

        EntityRequest updatedEntity = initialEntity.cloneEntity();
        updatedEntity.setVerified(true);

        ValidatableResponse response = apiClient.create(initialEntity);
        int createdEntityId = Integer.parseInt(response.extract()
                .body()
                .asString());

        apiClient.patch(createdEntityId, updatedEntity);

        EntityResponse actual = apiClient.getById(createdEntityId);
        EntityResponse expected = EntityResponse.create(createdEntityId, updatedEntity);
        assertEquals(expected, actual, "Updated entity does not match the expected one");
    }
}
