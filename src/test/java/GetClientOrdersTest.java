import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.requestsBody.OrderData;
import org.example.requestsBody.UserDataToCreate;
import org.example.responsesBody.Data;
import org.example.responsesBody.IngredientsResponse;
import org.example.responsesBody.LoginDataInResponse;
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
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Step("Send POST request to api/auth/register")
    public Response createUser(UserDataToCreate userDataToCreate) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(userDataToCreate)
                        .when()
                        .post("api/auth/register");
        return response;
    }
    @Step("Send DELETE request to /api/auth/user")
    public void deleteUser(String token) {
        given()
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .when()
                .delete("api/auth/user");
    }
    @Step
    public Response createOrder(OrderData orderData, String token){
        Response response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .body(orderData)
                .when()
                .post("api/orders");
        return response;
    }
    @Step
    public String getHash(int number) {
        Response response2 = given()
                .get("api/ingredients");
        IngredientsResponse ingredientsResponse = response2.as(IngredientsResponse.class);
        List<Data> ingredients = ingredientsResponse.getData();
        Data ingredient = ingredients.get(number);
        return ingredient.get_id();
    }
    @Test
    @DisplayName("Получение заказое авторизованного пользователя")
    public void getAuthClientOrders() {
        UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        String token = userData.getAccessToken();

        List<String> ingredients = new ArrayList<>();
        OrderData orderData = new OrderData(ingredients);
        createOrder(orderData, token);

        Response ordersList = given()
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .get("api/orders");

        ordersList.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());

        deleteUser(token);
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
