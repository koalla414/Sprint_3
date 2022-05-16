package Order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void setup() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов.")
    @Description("Регистрируем заказ, проверяем, что вернулись не пустые данные.")
    public void isOrderListReturn() {
        OrderResponse response = orderClient.getOrderList(); // регистрация заказа - отправили сгенерированные данные на ручку АПИ
        assertNotNull(response.getOrders());
    }

}