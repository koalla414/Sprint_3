package courier;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredClient {

    protected final String URL = "https://qa-scooter.praktikum-services.ru/api/v1";

    protected final RequestSpecification regSpec = given()
            //.header("Content-Type", "application/json")
            .contentType(ContentType.JSON)
            .baseUri(URL);

}

