import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.responsesBody.LoginDataInResponse;
import org.example.requestsBody.UserDataToLogin;
import org.example.requestsBody.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class AuthorizationTest {
    private String email = "lololololol@yandex.ru";
    private String password = "123";
    private String name = "Alhaitham";
    UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);

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
    @Step("Send DELETE request to /api/auth/user")
    public void deleteUser(String token) {
                given()
                        .auth().oauth2(token.replaceFirst("Bearer ",""))
                        .when()
                        .delete("api/auth/user");
    }
    @Test
    @DisplayName("Успешная авторизация")
    public void loginUserSuccessTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin(email, password);
        createUser(userDataToCreate);
        Response response = loginUser(userDataToLogin);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
        deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка авторизации: неправильнный e-mail")
    public void loginUserErrorEmailNullTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin("", password);
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        loginUser(userDataToLogin)
                 .then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
        deleteUser(userData.getAccessToken());
    }

    @Test
    @DisplayName("Ошибка авторизации: неправильный пароль")
    public void loginUserErrorPasswordNullTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin(email, "");
        Response response = createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        loginUser(userDataToLogin)
                .then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
        deleteUser(userData.getAccessToken());
    }
}
