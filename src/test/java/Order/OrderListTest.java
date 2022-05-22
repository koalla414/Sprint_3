package Order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        ExtractableResponse<Response> listResponse = orderClient.getOrderList();
        assertEquals(200, listResponse.statusCode());
        OrderResponse response = listResponse.body().as(OrderResponse.class);
        assertNotNull(response.getOrders());
    }

}