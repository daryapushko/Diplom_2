package ru.yandex.praktikum.user.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserGenerator;
import ru.yandex.praktikum.model.UserLogin;
import ru.yandex.praktikum.model.UserResponse;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

public class TestValidUserCreation {
    private UserClient userClient;
    private UserResponse userResponse;
    private int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @DisplayName("Создать уникального пользователя")
    @Description("Проверить, что пользователя можно создать;\n" +
            "    - чтобы создать пользователя, нужно передать в ручку все обязательные поля;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - успешный запрос возвращает ожидаемое тело ответа")
    @Test
    public void shouldCreateNewUserAndCheckResponse() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);

        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCode = loginResponse.extract().statusCode();
        userResponse = loginResponse.extract().as(UserResponse.class);

        assertEquals("The status code for registration is invalid", HTTP_OK, statusCode);
        assertTrue(userResponse.isSuccess());
        assertEquals(user.getEmail(), userResponse.getUser().getEmail());
        assertEquals(user.getName(), userResponse.getUser().getName());
        assertNotNull("No access token", userResponse.getAccessToken());
        assertNotNull("No refresh token", userResponse.getRefreshToken());
        assertEquals("The status code for login is invalid", HTTP_OK, statusCode);
    }

    @After
    public void clearDown() {
        userClient.deleteUser(userResponse);
    }
}
