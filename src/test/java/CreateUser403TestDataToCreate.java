import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
@RunWith(Parameterized.class)
public class CreateUser403Test {
        private final String email;
        private final String password;
        private final String name;
        public CreateUser403Test(String email, String password, String name) {
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
        public Response createUser(User user) {
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .body(user)
                            .when()
                            .post("api/auth/register");
            return response;
        }

        @Test
        public void createUserErrorUserDataNull() {
            User user = new User(email, password, name);
            createUser(user)
                    .then().statusCode(403)
                    .and()
                    .assertThat().body("message", equalTo("Email, password and name are required fields"));
        }
}
