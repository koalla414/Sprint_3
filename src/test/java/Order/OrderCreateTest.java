package Order;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private OrderClient orderClient;
    private int trackOrder;
    private String[] selectedColor;

    public OrderCreateTest(String[] selectedColor, String testName) {
        this.selectedColor = selectedColor;
    }

    @Before
    public void setup() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] isOrderCreated() {
        return new Object[][]{
                {new String[]{"BLACK"}, "Черный"},
                {new String[]{"GREY"}, "Серый"},
                {new String[]{"BLACK", "GREY"}, "Черный и серый"},
                {new String[]{}, "Цвет не выбран"},
        };
    }

    @Test
    @DisplayName("Можно создать заказ с разными вариантами выбора цвета самоката.")
    @Description("Генерируем данные для заказа, указываем цвет, регистрируем заказ, проверяем, что id ненулевой.")
    public void shouldCreateOrderWithColor() {
        Order order = Order.getRandomOrder(); // сгенерировали данные заказа
        order.setColor(selectedColor); // установили выбранный цвет
        int created = orderClient.create(order); // регистрация заказа - отправили сгенерированные данные на ручку АПИ

        trackOrder = orderClient.create(order);

        assertTrue(trackOrder > 0);
    }

}