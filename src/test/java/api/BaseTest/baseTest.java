package api.BaseTest;

// import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

public class baseTest {

    protected static final String REQRES_BASE_URL = "https://reqres.in/api";
    protected static final String JSONPLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com";

    protected static RequestSpecification reqresRequestSpec;
    protected static RequestSpecification jsonPlaceholderRequestSpec;
    protected static ResponseSpecification jsonResponseSpec;

    @BeforeAll
    public static void setup() {
        // Common setup for logging and reporting
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // Reqres API request specification
        reqresRequestSpec = new RequestSpecBuilder()
                .setBaseUri(REQRES_BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

        // JSONPlaceholder API request specification
        jsonPlaceholderRequestSpec = new RequestSpecBuilder()
                .setBaseUri(JSONPLACEHOLDER_BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

        // Common JSON response specification
        jsonResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}