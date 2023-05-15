package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import site.nomoreparties.stellarburgers.requests.UserDataToLogin;
import site.nomoreparties.stellarburgers.requests.UserDataToUpdate;

import static io.restassured.RestAssured.given;

public class Client {
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

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(UserDataToLogin data) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(data)
                        .when()
                        .post("api/auth/login");
        return response;
    }
    @Step("Send DELETE request to /api/auth/user")
    public void deleteUser(String token) {
        given()
                .auth().oauth2(token.replaceFirst("Bearer ",""))
                .when()
                .delete("api/auth/user");
    }
    @Step("Send PATCH request to api/auth/user")
    public Response updateUser(String token, UserDataToUpdate data) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(token.replaceFirst("Bearer ",""))
                        .and()
                        .body(data)
                        .when()
                        .patch("api/auth/user");
        return response;
    }
}
