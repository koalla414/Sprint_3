package courier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(Courier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();
    }

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier);
    }

}