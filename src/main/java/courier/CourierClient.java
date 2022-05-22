package courier;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;

public class CourierClient extends RestAssuredClient{

    private final String ROOT = "/courier";
    private final String LOGIN = ROOT + "/login";
    private final String COURIER = ROOT + "/{courierId}";

    public ExtractableResponse<Response> create(Courier courier) {
        return regSpec
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> login(CourierCredentials creds) {
        return regSpec
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> delete(int courierId) {
        HashMap params = new HashMap<String, Number>();
        params.put("courierId",  (courierId));
        return regSpec
                .when()
                .delete(COURIER, params)
                .then().log().all()
                .extract();
    }

}
