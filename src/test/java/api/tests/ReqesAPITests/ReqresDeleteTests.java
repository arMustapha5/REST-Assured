package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Epic("RESTful API Testing")
@Feature("DELETE Operations")
public class ReqresDeleteTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Management")
    @DisplayName("Delete an existing user")
    @Description("Test to verify that a user can be successfully deleted with a 204 status code")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUser() {
        Allure.step("Start test: Delete User API");

        Allure.step("Send DELETE request to remove user with ID 2", () -> {
            Response response =
                    given()
                            .when()
                            .delete(BASE_URL + "/users/2")
                            .then()
                            .statusCode(204)  // Expected 204 No Content for DELETE
                            .extract().response();

            logResponseDetails(response);
            Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
        });

        Allure.step("Verify the user has been deleted");

        Allure.step("Finish testing user deletion");
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Delete a non-existing user")
    @Description("Test to verify the behavior when attempting to delete a non-existing user")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistingUser() {
        Allure.step("Start test: Delete Non-Existing User API");

        Allure.step("Send DELETE request to remove non-existing user with ID 999", () -> {
            Response response =
                    given()
                            .when()
                            .delete(BASE_URL + "/users/999")
                            .then()
                            .statusCode(204)  // Reqres still returns 204 even for non-existent users
                            .extract().response();

            logResponseDetails(response);
            Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
        });

        Allure.step("Finish testing non-existing user deletion");
    }

    @Attachment(value = "API Response Details", type = "text/plain")
    private static String logResponseDetails(Response response) {
        StringBuilder details = new StringBuilder();
        details.append("Status Code: ").append(response.statusCode()).append("\n");
        details.append("Status Line: ").append(response.statusLine()).append("\n");
        details.append("Headers: ").append(response.headers()).append("\n");
        details.append("Response Body: ").append(response.getBody().asString()).append("\n");
        details.append("Response Time: ").append(response.time()).append(" ms");

        return details.toString();
    }
}