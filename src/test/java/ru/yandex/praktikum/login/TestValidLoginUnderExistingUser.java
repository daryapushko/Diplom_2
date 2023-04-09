package ru.yandex.praktikum.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserResponse;
import ru.yandex.praktikum.model.UserGenerator;
import ru.yandex.praktikum.model.UserLogin;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

public class TestValidLoginUnderExistingUser {
    private UserClient userClient;
    private UserResponse userResponse;
    private int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @DisplayName("Логин под существующим пользователем")
    @Description("Проверить, что пользователь может залогиниться;\n" +
            "    - чтобы залогинить пользователя, нужно передать в ручку все обязательные поля;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - успешный запрос возвращает ожидаемое тело ответа")
    @Test
    public void shouldLoginUnderExistingUserAndCheckResponse() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCode = loginResponse.extract().statusCode();
        userResponse = loginResponse.extract().as(UserResponse.class);

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue(userResponse.isSuccess());
        assertEquals(user.getEmail(), userResponse.getUser().getEmail());
        assertEquals(user.getName(), userResponse.getUser().getName());
        assertNotNull("No access token", userResponse.getAccessToken());
        assertNotNull("No refresh token", userResponse.getRefreshToken());
    }

    @After
    public void clearDown() {
        userClient.deleteUser(userResponse);
    }
}
