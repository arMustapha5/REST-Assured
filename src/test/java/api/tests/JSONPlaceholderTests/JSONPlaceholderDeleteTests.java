package api.tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("JSONPlaceholder API Testing")
@Feature("DELETE Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JSONPlaceholderDeleteTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    @Story("Post Management")
    @DisplayName("Delete an existing post")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePost() {
        executeDeleteTest("/posts/1", "Delete Existing Post");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Delete a non-existing post")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistingPost() {
        executeDeleteTest("/posts/9999", "Delete Non-Existing Post");
    }

    private void executeDeleteTest(String endpoint, String testName) {
        Allure.step("Start test: " + testName);

        Response response = given()
                .when()
                .delete(BASE_URL + endpoint)
                .then()
                .statusCode(200) // JSONPlaceholder returns 200 even for non-existing resources
                .extract().response();

        Allure.addAttachment("API Request", "text/plain", "DELETE " + endpoint);
        attachResponseDetails(response);

        Allure.step("Finish test: " + testName);
    }

    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers());
    }
}
