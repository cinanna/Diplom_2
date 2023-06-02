package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.requests.OrderData;
import site.nomoreparties.stellarburgers.responses.Data;
import site.nomoreparties.stellarburgers.responses.IngredientsResponse;
import java.util.List;
import static io.restassured.RestAssured.given;

public class Order {
    @Step("Send POST request to api/orders")
    public Response createOrder(OrderData orderData, String token){
        Response response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .body(orderData)
                .when()
                .post("api/orders");
        return response;
    }
    @Step("Send GET request to api/ingredients")
    public String getHash(int number) {
        Response response2 = given()
                .get("api/ingredients");
        IngredientsResponse ingredientsResponse = response2.as(IngredientsResponse.class);
        List<Data> ingredients = ingredientsResponse.getData();
        Data ingredient = ingredients.get(number);
        return ingredient.get_id();
    }
}
