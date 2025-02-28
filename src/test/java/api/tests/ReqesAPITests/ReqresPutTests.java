package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")
@Feature("PUT/PATCH Operations")
public class ReqresPutTests {

    private final String BASE_URL = "https://reqres.in/api";

    @Test
    @DisplayName("Update a user using PUT request")
    @Story("User Updates")
    @Description("Test PUT request to update an existing user on reqres.in")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserUsingPut() {
        Allure.step("Start test: Update User with PUT API");

        Allure.step("Prepare request payload with updated user details", () -> {
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", "John Updated");
            requestBody.put("job", "Software Engineer");

            Allure.addAttachment("Request Payload", "application/json", requestBody.toString());
        });

        // Create JSON payload for updating a user
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "John Updated");
        requestBody.put("job", "Software Engineer");

        Allure.step("Send PUT request to update user with ID 2", () -> {
            // Send PUT request and validate response
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody.toString())
                    .when()
                    .put(BASE_URL + "/users/2")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("name", equalTo("John Updated"))
                    .body("job", equalTo("Software Engineer"))
                    // Verify that the response contains an updated timestamp
                    .body("updatedAt", notNullValue())
                    .extract().response();

            logResponseDetails(response);

            // Capture updated timestamp
            String updatedAt = response.jsonPath().getString("updatedAt");
            Allure.addAttachment("Updated Timestamp", "text/plain", updatedAt);

            Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
            Assertions.assertEquals("John Updated", response.jsonPath().getString("name"), "Name should be updated");
            Assertions.assertEquals("Software Engineer", response.jsonPath().getString("job"), "Job should be updated");
        });

        Allure.step("Verify user data was updated correctly");

        Allure.step("Finish testing user update with PUT");
    }

    @Test
    @DisplayName("Update partial user data using PATCH")
    @Story("Partial Updates")
    @Description("Test PATCH request to partially update a user")
    @Severity(SeverityLevel.NORMAL)
    public void testPartialUpdateUsingPatch() {
        Allure.step("Start test: Partial Update User with PATCH API");

        Allure.step("Prepare request payload with only job field", () -> {
            JSONObject requestBody = new JSONObject();
            requestBody.put("job", "Senior Developer");

            Allure.addAttachment("Request Payload", "application/json", requestBody.toString());
        });

        // Create JSON payload with only the fields to update
        JSONObject requestBody = new JSONObject();
        requestBody.put("job", "Senior Developer");

        Allure.step("Send PATCH request to partially update user with ID 2", () -> {
            // Send PATCH request and validate response
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody.toString())
                    .when()
                    .patch(BASE_URL + "/users/2")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("job", equalTo("Senior Developer"))
                    .body("updatedAt", notNullValue())
                    .extract().response();

            logResponseDetails(response);

            // Add assertions
            Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
            Assertions.assertEquals("Senior Developer", response.jsonPath().getString("job"), "Job should be updated");
            Assertions.assertNotNull(response.jsonPath().getString("updatedAt"), "Updated timestamp should be present");
        });

        Allure.step("Verify user data was partially updated successfully");

        Allure.step("Finish testing partial user update with PATCH");
    }

    @Attachment(value = "API Response Details", type = "text/plain")
    private String logResponseDetails(Response response) {
        StringBuilder details = new StringBuilder();
        details.append("Status Code: ").append(response.statusCode()).append("\n");
        details.append("Status Line: ").append(response.statusLine()).append("\n");
        details.append("Headers: ").append(response.headers()).append("\n");
        details.append("Response Body: ").append(response.getBody().asString()).append("\n");
        details.append("Response Time: ").append(response.time()).append(" ms");

        return details.toString();
    }
}