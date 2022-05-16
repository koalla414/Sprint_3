package Order;

import courier.RestAssuredClient;

public class OrderClient extends RestAssuredClient {

    private final String ROOT = "/orders";

    public int create(Order order) {
        return regSpec
                .body(order)
                .when()
                .post(ROOT)
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");
    }

    public OrderResponse getOrderList() {
        return regSpec
                .when()
                .get(ROOT)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .body().as(OrderResponse.class);
    }

}