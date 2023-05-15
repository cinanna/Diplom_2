import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.Client;
import site.nomoreparties.stellarburgers.responses.LoginDataInResponse;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import site.nomoreparties.stellarburgers.requests.UserDataToUpdate;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTest {
    String newEmail = "academysumeru@yandex.ru";
    String newName = "Alhaitham";
    private String email = "academyblabla@yandex.ru";
    private String password = "1234";
    private String name = "Azar";
    Client client = new Client();
    UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Успешное обновление почты")
    public void updateUserEmailSuccessTest() {
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setEmail(newEmail);
        client.updateUser(userData.getAccessToken(), userDataToUpdate)
                .then().statusCode(200)
                .and()
                .assertThat().body("user.email", equalTo(newEmail));
        client.deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Успешное обновления имени")
    public void updateUserNameSuccessTest() {
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setName(newName);
        client.updateUser(userData.getAccessToken(), userDataToUpdate)
                .then().statusCode(200)
                .and()
                .assertThat().body("user.name", equalTo(newName));
        client.deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка обновления данных: не авторизован")
    public void updateUserDataFailTest() {
        Response response = client.createUser(userDataToCreate);
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        UserDataToUpdate userDataToUpdate = new UserDataToUpdate();
        userDataToUpdate.setName(newName);
        client.updateUser("Bearer ", userDataToUpdate)
                .then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
        client.deleteUser(userData.getAccessToken());
    }
}
