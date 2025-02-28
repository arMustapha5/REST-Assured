package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")
@Feature("GET Operations")
public class ReqresGetTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Listing")
    @DisplayName("Get list of users")
    @Description("Test to verify the API returns a list of users with pagination")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetUsers() {
        Allure.step("Start test: Get Users API");

        Allure.step("Send GET request to fetch users from page 2", () -> {
            Response response = RestAssured.get(BASE_URL + "/users?page=2");
            logResponseDetails(response);

            Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
            Assertions.assertTrue(response.getBody().asString().contains("data"), "Response should contain 'data' field");
        });

        Allure.step("Verify response contains expected data structure");

        Allure.step("Finish testing users list retrieval");
    }

    @Test
    @Story("User Details")
    @DisplayName("Get single user details")
    @Description("Test to verify the API returns correct details for a specific user")
    @Severity(SeverityLevel.CRITICAL)
    public void testSingleUser() {
        Allure.step("Start test: Get Single User API");

        Allure.step("Send GET request to fetch user with ID 2", () -> {
            Response response = given()
                    .when()
                    .get(BASE_URL + "/users/2")
                    .then()
                    .statusCode(200)
                    .body("data.id", equalTo(2))
                    .body("data.email", containsString("@"))
                    .extract().response();

            logResponseDetails(response);
        });

        Allure.step("Verify user details are correct");

        Allure.step("Finish testing single user retrieval");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Get non-existing user")
    @Description("Test to verify the API returns 404 for non-existing users")
    @Severity(SeverityLevel.NORMAL)
    public void testUserNotFound() {
        Allure.step("Start test: Get Non-Existing User API");

        Allure.step("Send GET request to fetch non-existing user with ID 999", () -> {
            Response response = given()
                    .when()
                    .get(BASE_URL + "/users/999")
                    .then()
                    .statusCode(404)
                    .extract().response();

            logResponseDetails(response);
            Assertions.assertEquals(404, response.statusCode(), "Status code should be 404");
        });

        Allure.step("Verify error response is received");

        Allure.step("Finish testing non-existing user retrieval");
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