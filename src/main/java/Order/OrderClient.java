package Order;

import courier.RestAssuredClient;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderClient extends RestAssuredClient {

    private final String ROOT = "/orders";

    public ExtractableResponse<Response> create(Order order) {
        return regSpec
                .body(order)
                .when()
                .post(ROOT)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> getOrderList() {
        return regSpec
                .when()
                .get(ROOT)
                .then().log().all()
                .extract();
    }

}