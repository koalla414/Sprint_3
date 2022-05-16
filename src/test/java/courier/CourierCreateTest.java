package courier;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя для авторизации
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными

        assertTrue(created); // проверяем, что пользователь создался
        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        courierClient.delete(courierId); // удаляем созданного пользователя
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров.")
    @Description("Создаем курьера, логинимся под ним, чтобы убедиться что действительно создан, повторно создаем курьера с теми же учетными данными, проверяем, что возвращается ошибка.")
    public void createDuplicateCourierIsConflict() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        boolean created = courierClient.create(courier); // регистрация пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя для авторизации
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными

        assertTrue(created); // проверяем, что пользователь создался
        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        courierClient.createWithDuplicateLogin(courier); // повторная регистрация того же пользователя - отправили сгенерированные данные на ручку АПИ
        courierClient.delete(courierId); // удаляем созданного пользователя
    }

    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку")
    @Description("Генерируем данные для регистрации курьера, удаляем пароль, регистрируемся без пароля, проверяем, что возвращается ошибка.")
    public void createCourierWithoutPasswordIsBadRequest() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        courier.setPassword(""); // удалили пароль
        courierClient.createWithoutLoginOrPassword(courier); // регистрируемся без пароля
    }

}