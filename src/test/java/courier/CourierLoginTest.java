package courier;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

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
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными

        courierClient.delete(courierId); // удаляем созданного пользователя
    }

    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль.")
    @Description("Создаем курьера, логинимся под ним, искажаем пароль в учетных данных, и логинимся с испорченным паролем, убеждаемся, что что возвращается ошибка.")
    public void loginWithModifiedPasswordIsNotFound() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        courier.setPassword(courier.getPassword()+"I"); // исказили пароль
        creds = CourierCredentials.from(courier); // получаем учетные данные с искаженным паролем

        courierClient.loginWithIncorrectData(creds); // логинимся с испорченным паролем

        courierClient.delete(courierId); // удаляем созданного пользователя
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку.")
    @Description("Создаем курьера, логинимся под ним, удаляем пароль в учетных данных, и логинимся с пустым паролем, убеждаемся, что что возвращается ошибка.")
    public void loginWithoutPasswordIsBadRequest() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными
        courier.setPassword(""); // удалили пароль

        creds = CourierCredentials.from(courier); // получаем учетные данные с искаженным паролем


        courierClient.loginWithoutLoginOrPassword(creds); // логинимся с пустым паролем

        courierClient.delete(courierId); // удаляем созданного пользователя
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку.")
    @Description("Создаем курьера, логинимся под ним, удаляем его, и логинимся под удаленным курьером, убеждаемся, что что возвращается ошибка.")
    public void loginWithNonexistentCourierIsNotFound() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными

        courierClient.delete(courierId); // удаляем созданного пользователя
        courierClient = new CourierClient();
        courierClient.loginWithIncorrectData(creds); // логинимся под удаленным пользователем
    }

    @Test
    @DisplayName("Успешный запрос возвращает id.")
    @Description("Создаем курьера, логинимся под ним, убеждаемся, что вернулся ID.")
    public void loginCourierSuccessfulReturnId() {
        Courier courier = Courier.getRandomCourier(); // сгенерировали данные пользователя
        boolean created = courierClient.create(courier); // регистрации пользователя - отправили сгенерированные данные на ручку АПИ

        CourierCredentials creds = CourierCredentials.from(courier); // получаем учетные данные созданного пользователя
        courierId = courierClient.login(creds); // получаем ID путем авторизации с полученными учетными данными

        assertNotEquals(0, courierId); // проверяем, что ID ненулевой

        courierClient.delete(courierId); // удаляем созданного пользователя
    }

}