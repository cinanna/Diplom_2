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
public class CreateOrderTest {
    private String email = "asgrse21@yandex.ru";
    private String password = "123456";
    private String name = "gthsth";
    UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
    Client client = new Client();
    Order order = new Order();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderAuthTest(){
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        String token = userData.getAccessToken();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(order.getHash(0));
        OrderData orderData = new OrderData(ingredients);
        Response newOrder = order.createOrder(orderData, token);
        newOrder.then()
                .statusCode(200)
                .and()
                .assertThat().body("name", notNullValue());
        client.deleteUser(token);
    }
    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderNoAuthTest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(order.getHash(0));
        OrderData orderData = new OrderData(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("api/orders");
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("name", notNullValue());
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderNoIngredients() {
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        String token = userData.getAccessToken();
        List<String> ingredients = new ArrayList<>();
        OrderData orderData = new OrderData(ingredients);
        Response newOrder = order.createOrder(orderData, token);
        newOrder.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));

        client.deleteUser(token);
    }
    @Test
    @DisplayName("Создание заказа с неправильным хэшем ингредиентов")
    public void createOrderNoHashError() {
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        String token = userData.getAccessToken();
        List<String> ingredients = new ArrayList<>();
        ingredients.add("123");
        OrderData orderData = new OrderData(ingredients);
        Response newOrder = order.createOrder(orderData, token);
        newOrder.then().statusCode(500);
        client.deleteUser(token);
    }
}