package api.tests.ReqesAPITests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")
@Feature("PUT Operations")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReqresPutTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Update")
    @DisplayName("Update user with full data using PUT")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserUsingPut() {
        executePutTest("/users/2", "Update User",
                new JSONObject().put("name", "John Updated").put("job", "Software Engineer"), 200);
    }

    @Test
    @Story("Partial Update")
    @DisplayName("Update user with partial data using PUT")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUserWithPartialData() {
        executePutTest("/users/2", "Update User with Partial Data",
                new JSONObject().put("job", "Senior Developer"), 200);
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
                response.then().body(key, equalTo(requestBody.getString(key)))
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
