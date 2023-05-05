import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.responsesBody.LoginDataInResponse;
import org.example.requestsBody.UserDataToCreate;
import org.example.requestsBody.UserDataToLogin;
import org.example.requestsBody.UserDataToUpdate;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTest {
    String newEmail = "academysumeru@yandex.ru";
    String newName = "Alhaitham";
    private String email = "academyblabla@yandex.ru";
    private String password = "1234";
    private String name = "Azar";

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

    @Step("Send DELETE request to /api/auth/user")
    public void deleteUser(String token) {
                given()
                        .auth().oauth2(token.replaceFirst("Bearer ",""))
                        .when()
                        .delete("api/auth/user");
    }
    @Test
    @DisplayName("Успешное обновление почты")
    public void updateUserEmailSuccessTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setEmail(newEmail);
        updateUser(userData.getAccessToken(), userDataToUpdate)
                .then().statusCode(200)
                .and()
                .assertThat().body("user.email", equalTo(newEmail));
        deleteUser(userData.getAccessToken());
    }

    @Test
    @DisplayName("Успешное обновления имени")
    public void updateUserNameSuccessTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setName(newName);
        updateUser(userData.getAccessToken(), userDataToUpdate)
                .then().statusCode(200)
                .and()
                .assertThat().body("user.name", equalTo(newName));
        deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка обновления данных: не авторизован")
    public void updateUserDataFailTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setName(newName);
        updateUser("Bearer ", userDataToUpdate)
                .then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
        deleteUser(userData.getAccessToken());
    }
}
