import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.responsesBody.LoginDataInResponse;
import org.example.requestsBody.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class CreateUserTest {

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

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest(){
        UserDataToCreate userDataToCreate = new UserDataToCreate("letmeinahahahahahah@yandex.ru", "123", "Nya");
        Response response = createUser(userDataToCreate);
        response.then().statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        deleteUser(userData.getAccessToken());
    }
    @Test
    @DisplayName("Ошибка создания пользователя: пользователь уже существует")
    public void createUserErrorUserExistTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate("blablaahaha@yandex.ru", "123", "Nya");
        Response response = createUser(userDataToCreate);
        createUser(userDataToCreate)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        deleteUser(userData.getAccessToken());
    }

}
