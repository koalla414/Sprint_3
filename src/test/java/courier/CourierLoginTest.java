package courier;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setup() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Курьер может авторизоваться.")
    @Description("Создаем курьера, логинимся под ним.")
    public void loginCourierSuccessful() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
    }

    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль.")
    @Description("Создаем курьера, логинимся под ним, искажаем пароль в учетных данных, и логинимся с испорченным паролем, убеждаемся, что что возвращается ошибка.")
    public void loginWithModifiedPasswordIsNotFound() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");
        courier.setPassword(courier.getPassword()+"I"); // исказили пароль
        creds = CourierCredentials.from(courier); // получаем учетные данные с искаженным паролем

        loginResponse = courierClient.login(creds); // логинимся с испорченным паролем
        assertEquals(404, loginResponse.statusCode());
        assertEquals("Учетная запись не найдена", loginResponse.path("message"));

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку.")
    @Description("Создаем курьера, логинимся под ним, удаляем пароль в учетных данных, и логинимся с пустым паролем, убеждаемся, что что возвращается ошибка.")
    public void loginWithoutPasswordIsBadRequest() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");
        courier.setPassword(""); // удалили пароль

        creds = CourierCredentials.from(courier); // получаем учетные данные с искаженным паролем


        loginResponse = courierClient.login(creds); // логинимся с пустым паролем
        assertEquals(400, loginResponse.statusCode());
        assertEquals("Недостаточно данных для входа", loginResponse.path("message"));

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку.")
    @Description("Создаем курьера, логинимся под ним, удаляем его, и логинимся под удаленным курьером, убеждаемся, что что возвращается ошибка.")
    public void loginWithNonexistentCourierIsNotFound() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
        courierClient = new CourierClient();

        loginResponse = courierClient.login(creds); // логинимся под удаленным курьером
        assertEquals(404, loginResponse.statusCode());
        assertEquals("Учетная запись не найдена", loginResponse.path("message"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id.")
    @Description("Создаем курьера, логинимся под ним, убеждаемся, что вернулся ID.")
    public void loginCourierSuccessfulReturnId() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");

        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
    }

}