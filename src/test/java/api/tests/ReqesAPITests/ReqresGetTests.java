package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")
@Feature("GET Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReqresGetTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Listing")
    @DisplayName("Get list of users")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUsers() {
        executeGetTest("/users?page=2", "Get User List");
    }

    @Test
    @Story("User Details")
    @DisplayName("Get single user details")
    @Severity(SeverityLevel.NORMAL)
    public void testSingleUser() {
        executeGetTest("/users/2", "Get Single User");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Get non-existing user")
    @Severity(SeverityLevel.MINOR)
    public void testUserNotFound() {
        executeGetTest("/users/999", "Get Non-Existing User", 404);
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
