import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.Client;
import site.nomoreparties.stellarburgers.Order;
import site.nomoreparties.stellarburgers.requests.OrderData;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import site.nomoreparties.stellarburgers.responses.LoginDataInResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class GetClientOrdersTest {
    private String email = "asgrse21@yandex.ru";
    private String password = "123456";
    private String name = "gthsth";
    Client client = new Client();
    Order order = new Order();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Получение заказое авторизованного пользователя")
    public void getAuthClientOrders() {
        UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        String token = userData.getAccessToken();

        List<String> ingredients = new ArrayList<>();
        OrderData orderData = new OrderData(ingredients);
        order.createOrder(orderData, token);

        Response ordersList = given()
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .get("api/orders");

        ordersList.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());

        client.deleteUser(token);
    }
    @Test
    @DisplayName("Получение заказое неавторизованного пользователя")
    public void getNoAuthClientOrders() {
        Response ordersList = given()
                .get("api/orders");

        ordersList.then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }
}