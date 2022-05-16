package Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private Integer metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public static Order getRandomOrder() {
        Random random = new Random();
        LocalDate futureDate = LocalDate.now().plusDays(random.nextInt(14));

        String firstName = RandomStringUtils.randomAlphanumeric(10);
        String lastName = RandomStringUtils.randomAlphanumeric(10);
        String address = RandomStringUtils.randomAlphanumeric(10);
        Integer metroStation = random.nextInt(225);
        String phone = "+7" + RandomStringUtils.randomNumeric(10);
        Integer rentTime = random.nextInt(7);
        String deliveryDate = futureDate.toString();
        String comment = RandomStringUtils.randomAlphabetic(10);
        String[] color = {"BLACK", "GREY"};

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

}