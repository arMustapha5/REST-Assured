package api.tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Testing")
@Feature("GET Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JSONPlaceholderGetTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    @Story("Post Listing")
    @DisplayName("Get all posts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPosts() {
        executeGetTest("/posts", "Get All Posts");
    }

    @Test
    @Story("Post Details")
    @DisplayName("Get a single post")
    @Severity(SeverityLevel.NORMAL)
    public void testGetSinglePost() {
        executeGetTest("/posts/1", "Get Single Post");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Get a non-existing post")
    @Severity(SeverityLevel.MINOR)
    public void testPostNotFound() {
        executeGetTest("/posts/9999", "Get Non-Existing Post", 404);
    }

    private void executeGetTest(String endpoint, String testName) {
        executeGetTest(endpoint, testName, 200);
    }

    private void executeGetTest(String endpoint, String testName, int expectedStatus) {
        Allure.step("Start test: " + testName);

        Response response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();

        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response);

        Allure.step("Finish test: " + testName);
    }

    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
