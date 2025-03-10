package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("RESTful API Testing")
@Feature("DELETE Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReqresDeleteTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Management")
    @DisplayName("Delete an existing user")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUser() {
        executeDeleteTest("/users/2", "Delete Existing User");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Delete a non-existing user")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistingUser() {
        executeDeleteTest("/users/999", "Delete Non-Existing User");
    }

    private void executeDeleteTest(String endpoint, String testName) {
        Allure.step("Start test: " + testName);

        Response response = given()
                .when()
                .delete(BASE_URL + endpoint)
                .then()
                .statusCode(204)
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
