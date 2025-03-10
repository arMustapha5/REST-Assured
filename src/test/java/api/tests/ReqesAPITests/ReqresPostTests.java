package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")
@Feature("POST Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReqresPostTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Creation")
    @DisplayName("Create a new user")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser() {
        executePostTest("/users", "Create User",
                new JSONObject().put("name", "Abdul Rehman").put("job", "QA Engineer"), 201);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Create user with invalid data")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithInvalidData() {
        executePostTest("/users", "Create User with Invalid Data",
                new JSONObject().put("name", ""), 201);
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

        if (requestBody.has("name")) {
            response.then().body("name", equalTo(requestBody.getString("name")));
        }
        if (requestBody.has("job")) {
            response.then().body("job", equalTo(requestBody.getString("job")));
        }

        Allure.step("Finish test: " + testName);
    }

    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
