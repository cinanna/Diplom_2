import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.Client;
import site.nomoreparties.stellarburgers.responses.LoginDataInResponse;
import site.nomoreparties.stellarburgers.requests.UserDataToLogin;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class AuthorizationTest {
    private String email = "lololololol@yandex.ru";
    private String password = "123";
    private String name = "Alhaitham";
    UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
    Client client = new Client();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Успешная авторизация")
    public void loginUserSuccessTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin(email, password);
        client.createUser(userDataToCreate);
        Response response = client.loginUser(userDataToLogin);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
        client.deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка авторизации: неправильнный e-mail")
    public void loginUserErrorEmailNullTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin("", password);
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        client.loginUser(userDataToLogin)
                 .then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
        client.deleteUser(userData.getAccessToken());
    }

    @Test
    @DisplayName("Ошибка авторизации: неправильный пароль")
    public void loginUserErrorPasswordNullTest() {
        UserDataToLogin userDataToLogin = new UserDataToLogin(email, "");
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        client.loginUser(userDataToLogin)
                .then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
        client.deleteUser(userData.getAccessToken());
    }
}
