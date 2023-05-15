import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.Client;
import site.nomoreparties.stellarburgers.responses.LoginDataInResponse;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class CreateUserTest {
    Client client = new Client();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest(){
        UserDataToCreate userDataToCreate = new UserDataToCreate("letmeinahahahahahah@yandex.ru", "123", "Nya");
        Response response = client.createUser(userDataToCreate);
        response.then().statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        client.deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка создания пользователя: пользователь уже существует")
    public void createUserErrorUserExistTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate("blablaahaha@yandex.ru", "123", "Nya");
        Response response = client.createUser(userDataToCreate);
        client.createUser(userDataToCreate)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        client.deleteUser(userData.getAccessToken());
    }
}