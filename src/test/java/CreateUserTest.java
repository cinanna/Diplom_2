import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.LoginDataInResponse;
import org.example.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class CreateUserTestDataToCreate {

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
    public Response deleteUser(String token) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2("token")
                        .when()
                        .delete("api/auth/register");
        return response;
    }

    @Test
    public void createUserSuccessTest(){
        UserDataToCreate userDataToCreate = new UserDataToCreate("letmeinahahah@yandex.ru", "123", "Nya");
        Response response = createUser(userDataToCreate);
        response.then().statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        deleteUser(userData.getAccessToken());
    }
    @Test
    public void createUserErrorUserExistTest() {
        UserDataToCreate userDataToCreate = new UserDataToCreate("letmeinahahah@yandex.ru", "123", "Nya");
        createUser(userDataToCreate);
        Response response = createUser(userDataToCreate);
        createUser(userDataToCreate)
                .then().statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
        LoginDataInResponse userData = response.as(LoginDataInResponse.class);
        deleteUser(userData.getAccessToken());
    }

}
