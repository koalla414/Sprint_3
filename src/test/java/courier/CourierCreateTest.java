package courier;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setup() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Курьера можно создать.")
    @Description("Создаем курьера, логинимся под ним, чтобы убедиться что действительно создан.")
    public void createRandomCourierSuccessful() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя для авторизации
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode());
        courierId = loginResponse.path("id");

        assertTrue(created); // проверяем, что пользователь создался
        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
        assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров.")
    @Description("Создаем курьера, логинимся под ним, чтобы убедиться что действительно создан, повторно создаем курьера с теми же учетными данными, проверяем, что возвращается ошибка.")
    public void createDuplicateCourierIsConflict() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        ExtractableResponse<Response> createResponse = courierClient.create(courier);
        assertEquals(201, createResponse.statusCode());
        boolean created = createResponse.path("ok"); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя для авторизации
        ExtractableResponse<Response> loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        assertEquals(200, loginResponse.statusCode()); //
        courierId = loginResponse.path("id"); // получили id

        assertTrue(created); // проверяем, что пользователь создался
        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        try {
            createResponse = courierClient.create(courier); // повторная регистрация того же пользователя - отправили сгенерированные данные на ручку АПИ
            assertEquals(409, createResponse.statusCode()); // сравнили статус-код
            assertEquals("Этот логин уже используется. Попробуйте другой.", createResponse.path("message")); // сравнили сообщение об ошибке
            ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
            assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
        } catch (Exception e) {
            ExtractableResponse<Response>  deleteResponse = courierClient.delete(courierId);
            assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
            loginResponse = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.path("id");
                deleteResponse = courierClient.delete(courierId);
                assertEquals(200, deleteResponse.statusCode());// удаляем созданного пользователя
            }
        }
    }

    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку")
    @Description("Генерируем данные для регистрации курьера, удаляем пароль, регистрируемся без пароля, проверяем, что возвращается ошибка.")
    public void createCourierWithoutPasswordIsBadRequest() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        courier.setPassword(""); // удалили пароль
        ExtractableResponse<Response> createResponse = courierClient.create(courier); // повторная регистрация того же пользователя - отправили сгенерированные данные на ручку АПИ
        assertEquals(400, createResponse.statusCode()); // сравнили статус-код
        assertEquals("Недостаточно данных для создания учетной записи", createResponse.path("message")); // сравнили сообщение об ошибке
        //courierClient.createWithoutLoginOrPassword(courier); // регистрируемся без пароля
    }

}