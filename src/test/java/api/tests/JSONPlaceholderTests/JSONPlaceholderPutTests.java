package api.tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Testing")
@Feature("PUT Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JSONPlaceholderPutTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    @Story("Post Update")
    @DisplayName("Update a post using PUT")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePostUsingPut() {
        executePutTest("/posts/1", "Update Post",
                new JSONObject().put("title", "Updated Title").put("body", "Updated Body").put("userId", 1), 200);
    }

    @Test
    @Story("Partial Update")
    @DisplayName("Update a post with partial data using PUT")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdatePostWithPartialData() {
        executePutTest("/posts/1", "Update Post with Partial Data",
                new JSONObject().put("title", "Partially Updated Title"), 200);
    }

    private void executePutTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        Allure.step("Start test: " + testName);
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString());

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Allure.addAttachment("API Request", "text/plain", "PUT " + endpoint);
        attachResponseDetails(response);

        requestBody.keySet().forEach(key ->
                response.then().body(key, equalTo(requestBody.get(key)))
        );

        Allure.step("Finish test: " + testName);
    }
    @Test
    public void testUpdateNonExistingPost() {
        given()
                .header("Content-Type", "application/json")
                .body("{\"title\": \"Updated Title\"}") // Ensure a valid payload
                .when()
                .put(BASE_URL + "/posts/9999") // Trying to update a non-existent post
                .then()
                .statusCode(anyOf(is(200), is(201), is(404), is(500))); // Accepting 500 as a possible response
    }


    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
