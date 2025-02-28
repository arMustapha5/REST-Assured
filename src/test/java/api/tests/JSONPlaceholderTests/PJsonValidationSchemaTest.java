package api.tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;

import static io.restassured.RestAssured.given;

@Epic("JSONPlaceholder API Testing")
@Feature("JSON Schema Validation")
public class PJsonValidationSchemaTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String SCHEMA_PATH = "src/test/resources/schemas/";

    @Test
    @Story("Schema Validation")
    @DisplayName("Validate Post List JSON Schema")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetJsonSchema() {
        executeSchemaValidationTest("/posts", "JUser-list-schema.json", "Validate Post List Schema");
    }

    @Test
    @Story("Schema Validation")
    @DisplayName("Validate Single Post JSON Schema")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllJsonSchema() {
        executeSchemaValidationTest("/posts/1", "Jsingle-user-schema.json", "Validate Single Post Schema");
    }

    private void executeSchemaValidationTest(String endpoint, String schemaFileName, String testName) {
        Allure.step("Start test: " + testName);

        Response response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .statusCode(200)
                .extract().response();

        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response);

        File schemaFile = new File(SCHEMA_PATH + schemaFileName);
        Allure.addAttachment("JSON Schema", "text/plain", schemaFile.getAbsolutePath());

        response.then().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));

        Allure.step("Finish test: " + testName);
    }

    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
