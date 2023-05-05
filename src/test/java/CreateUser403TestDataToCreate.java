import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.requestsBody.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
@RunWith(Parameterized.class)
public class CreateUser403TestDataToCreate {
        private final String email;
        private final String password;
        private final String name;
        public CreateUser403TestDataToCreate(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }
        @Parameterized.Parameters
        public static Object[][] getTestData() {
            return new Object[][] {
                    { "myMail@yandex.ru", "12345", null},
                    { "yourMail@yandex.ru", null, "Nya"},
                    { null, "54321", "Nya"},
            };
        }

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

        @Test
        @DisplayName("Ошибка создания пользователя: не хватает данных")
        public void createUserErrorUserDataNullTest() {
            UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
            createUser(userDataToCreate)
                    .then().statusCode(403)
                    .and()
                    .assertThat().body("message", equalTo("Email, password and name are required fields"));
        }
}
