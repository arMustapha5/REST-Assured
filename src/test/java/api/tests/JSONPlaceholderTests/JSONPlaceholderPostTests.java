package api.tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Testing")
@Feature("POST Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JSONPlaceholderPostTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    @Story("Post Creation")
    @DisplayName("Create a new post")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePost() {
        executePostTest("/posts", "Create Post",
                new JSONObject().put("title", "Test Post").put("body", "This is a test post.").put("userId", 1), 201);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Create a post with missing fields")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePostWithMissingFields() {
        executePostTest("/posts", "Create Post with Missing Fields",
                new JSONObject().put("title", ""), 201);
    }

    private void executePostTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        Allure.step("Start test: " + testName);
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString());

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Allure.addAttachment("API Request", "text/plain", "POST " + endpoint);
        attachResponseDetails(response);

        requestBody.keySet().forEach(key ->
                response.then().body(key, equalTo(requestBody.get(key)))
        );

        Allure.step("Finish test: " + testName);
    }

    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
