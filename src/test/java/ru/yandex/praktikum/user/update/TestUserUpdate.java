package ru.yandex.praktikum.user.update;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class TestUserUpdate {
        private UserClient userClient;
        private AuthResponse authResponse;
        private ErrorResponse errorResponse;
        private UserResponse userResponse;
        private int statusCode;
        private final String email;
        private final String password;
        private final String name;

    public TestUserUpdate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters(name = "Email, password и name. Тестовые данные: {0} {1} {2}")
    public static Object[][] getUserData() {
        return new Object[][]{
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", "", ""},
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(5), ""},
                {"", RandomStringUtils.randomAlphanumeric(5), ""},
                {"", "", RandomStringUtils.randomAlphabetic(5)},
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", "", RandomStringUtils.randomAlphanumeric(5)},
                {"", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphabetic(5)},
                {"", "", ""}
        };
    }
        @Before
        public void setUp() {
            userClient = new UserClient();
        }
        @After
        public void clearDown() {
        userClient.deleteUser(userResponse);
    }

        @DisplayName("Изменение даннных пользователя")
        @Description("Проверить, что поля пользователя можно изменить;\n" +
                "    - чтобы изменить пользователя, нужно передать в ручку все обязательные поля;\n" +
                "    - запрос содержит авторизацию;\n" +
                "    - запрос возвращает правильный код ответа;\n" +
                "    - успешный запрос возвращает ожидаемое тело ответа")
        @Test
        public void shouldUpdateUserDataAndCheckResponse() {
            User user = UserGenerator.getRandomData();
            ValidatableResponse createResponse = userClient.createNewUser(user);
            statusCode = createResponse.extract().statusCode();
            userResponse = createResponse.extract().as(UserResponse.class);

            if (!this.email.equals("")){
                user.setEmail(this.email);
            }
            if(!this.password.equals("")){
                user.setPassword(this.password);
            }
            if(!this.name.equals("")){
                user.setName(this.name);
            }

            ValidatableResponse updateResponse = userClient.updateUser(user, userResponse);
            authResponse = updateResponse.extract().as(AuthResponse.class);

            assertEquals("The status code is invalid", HTTP_OK, statusCode);
            assertTrue(authResponse.isSuccess());
            assertEquals(user.getEmail().toLowerCase(), authResponse.getUser().getEmail());
            assertEquals(user.getName(), authResponse.getUser().getName());
        }

    @DisplayName("Запрещено изменение даннных пользователя без авторизационного токена")
    @Test
    public void shouldNotUpdateUserDataWithoutAuthorisation() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);

        if (!this.email.equals("")){
            user.setEmail(this.email);
        }
        if(!this.password.equals("")){
            user.setPassword(this.password);
        }
        if(!this.name.equals("")){
            user.setName(this.name);
        }

        ValidatableResponse updateResponse = userClient.unauthRequestUpdateUser(user);
        statusCode = updateResponse.extract().statusCode();
        errorResponse = updateResponse.extract().as(ErrorResponse.class);

        assertEquals("The status code is invalid", HTTP_UNAUTHORIZED, statusCode);
        assertFalse(errorResponse.isSuccess());
    }
}
