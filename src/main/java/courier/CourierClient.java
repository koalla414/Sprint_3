package courier;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

public class CourierClient extends RestAssuredClient{

    private final String ROOT = "/courier";
    private final String LOGIN = ROOT + "/login";
    private final String COURIER = ROOT + "/{courierId}";

    public boolean create(Courier courier) {
        return regSpec
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
    }

    public void createWithDuplicateLogin(Courier courier) {
         regSpec
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    public void createWithoutLoginOrPassword(Courier courier) {
        regSpec
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    public int login(CourierCredentials creds) {
        return regSpec
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public void loginWithIncorrectData(CourierCredentials creds) {
        regSpec
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all()
                .assertThat().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    public void loginWithoutLoginOrPassword(CourierCredentials creds) {
        regSpec
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    public void delete(int courierId) {
        HashMap params = new HashMap<String, Number>();
        params.put("courierId",  (courierId));
        regSpec
//                .pathParams("courierId", String.valueOf(courierId))
                .when()
                .delete(COURIER, params)
                .then().log().all()
                .assertThat()
                .statusCode(200);
    }

}
