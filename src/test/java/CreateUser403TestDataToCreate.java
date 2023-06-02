import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import site.nomoreparties.stellarburgers.Client;
import site.nomoreparties.stellarburgers.requests.UserDataToCreate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        Client client = new Client();
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

        @Test
        @DisplayName("Ошибка создания пользователя: не хватает данных")
        public void createUserErrorUserDataNullTest() {
            UserDataToCreate userDataToCreate = new UserDataToCreate(email, password, name);
            client.createUser(userDataToCreate)
                    .then().statusCode(403)
                    .and()
                    .assertThat().body("message", equalTo("Email, password and name are required fields"));
        }
}