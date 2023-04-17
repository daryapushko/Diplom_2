package ru.yandex.praktikum.user.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestCreatingExistingUserShouldFail {
    private UserClient userClient;
    private UserResponse userResponse;
    private ErrorResponse errorResponse;
    private int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    @Description("Проверить, что того же пользователя нельзя создать;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - запрос возвращает ожидаемое тело ответа")
    @Test
    public void shouldNotCreateTheSameUser() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        ValidatableResponse createResponseForTheSameUser = userClient.createNewUser(user);
        errorResponse = createResponseForTheSameUser.extract().as(ErrorResponse.class);
        statusCode = createResponseForTheSameUser.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_FORBIDDEN, statusCode);
        assertFalse(errorResponse.isSuccess());

        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        userResponse = loginResponse.extract().as(UserResponse.class);
    }

    @After
    public void clearDown() {
        userClient.deleteUser(userResponse);
    }
}
